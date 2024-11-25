package mq.inventario.service;

import jakarta.transaction.Transactional;
import mq.inventario.models.DetalleVenta;
import mq.inventario.models.Producto;
import mq.inventario.models.Venta;
import mq.inventario.dto.VentaRequest;
import mq.inventario.repository.ProductoRepository;
import mq.inventario.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaServicio implements IVentaServicio {
    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;



    @Override
    public Venta realizarVenta(List<Long> productosIds) {
        //Obtenemos todos los productos seleccionados
        List<Producto> productos = productoRepository.findAllById(productosIds);

        //calcular el total de la venta
        Double totalVenta = productos.stream()
                .mapToDouble(Producto::getPrecioProducto)
                .sum();

        //crea la venta
        Venta venta = new Venta();
        venta.setProductos(productos);
        venta.setTotalVenta(totalVenta);

        //Guardar la venta
        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("Venta no encontrada con ID: " + id));
    }

    @Transactional
    public void guardarVenta(VentaRequest request){
        //Crear y guardar la entiad Venta
        Venta venta = new Venta();
        venta.setTotalVenta(request.getTotal());
        venta.setFechaVenta(LocalDateTime.now());

        //Convertir los detalles y aspciarlos a la venta
        List<DetalleVenta>detalles = request.getProductos().stream()
                .map(detalle ->{
                    DetalleVenta dv = new DetalleVenta();
                    dv.setNombre(detalle.getNombre());
                    dv.setCantidad(detalle.getCantidad());
                    dv.setPrecio(detalle.getPrecio());
                    dv.setVenta(venta);
                    return dv;
                })
                .collect(Collectors.toList());
        venta.setDetalles(detalles);

        //Guardar en la base de datos
        ventaRepository.save(venta);
    }
}
