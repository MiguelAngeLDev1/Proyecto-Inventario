package mq.inventario.service;

import mq.inventario.models.Producto;
import mq.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServicio implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;


    @Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }


    @Override
    public Producto agregarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarProducto(Producto producto) {
        Producto productoExistente = productoRepository.findById(producto.getId())
                .orElseThrow(()->new IllegalArgumentException("No existe el producto con id: " + producto.getId()));

        //Se actualian los campos necesarios
        productoExistente.setNombreProducto(producto.getNombreProducto());
        productoExistente.setDescripcionProducto(producto.getDescripcionProducto());
        productoExistente.setPrecioProducto(producto.getPrecioProducto());
        productoExistente.setCategoria(producto.getCategoria());
        return productoRepository.save(productoExistente);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
