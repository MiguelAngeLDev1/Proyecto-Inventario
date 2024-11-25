package mq.inventario.controler;


import mq.inventario.dto.ProductoVentaDTO;
import mq.inventario.dto.VentaDTO;
import mq.inventario.models.Producto;
import mq.inventario.models.Venta;
import mq.inventario.dto.VentaRequest;
import mq.inventario.repository.VentaRepository;
import mq.inventario.service.ProductoServicio;
import mq.inventario.service.VentaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/ventas")
public class VentaControler {



    @Autowired
    private VentaServicio ventaServicio;
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private VentaRepository ventaRepository;


    //Mostrar Formulario para realizar una venta
    @GetMapping("/realizar")
    public String mostrarFormularioVenta(Model model) {

        //Obtener todos los productos disponibles
        List<Producto> productos = productoServicio.listar();
        VentaDTO ventaDTO = new VentaDTO();

        //Verificar si detallesVenta es null y si es asi, Inicialisarlo
        if (ventaDTO.getDetallesVenta() == null){
            ventaDTO.setDetallesVenta(new HashMap<>());
        }

        //Inicializa 'detallesVenta' en el DTO
        productos.forEach(producto -> ventaDTO.getDetallesVenta().put(producto.getId(),new ProductoVentaDTO()));

        model.addAttribute("productos", productos);
        model.addAttribute("ventaDTO",  ventaDTO);

        return "listarVentas";//Vista del formulario para hacer la venta
    }

    @PostMapping("/realizarVenta")
    @ResponseBody
    public ResponseEntity<String> realizarVenta(@RequestBody VentaDTO ventaDTO) {
        try {
            //Procesar la venta y guardarla en la base de datos
            ventaServicio.realizarVenta((List<Long>) ventaDTO);
            return ResponseEntity.ok("Venta realizado con exito");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al realizar la venta");
        }
    }

    //Procesar la venta y descontar inventario
    @PostMapping("/realizarVenta/form")
    public String realizarVenta(@ModelAttribute VentaDTO ventaDTO, RedirectAttributes redirectAttributes) {
        //procesar cada producto en la venta
        ventaDTO.getDetallesVenta().forEach((productoId,detalle)->{
            Producto producto = productoServicio.obtenerProductoPorId(productoId);
            int cantidadActual = producto.getCantidadProducto();

            if (cantidadActual >= detalle.getCantidad()){
                //Descontar la cantidad vendida del inventario
                producto.setCantidadProducto(cantidadActual - detalle.getCantidad());
                productoServicio.agregarProducto(producto);
            }else {
                redirectAttributes.addFlashAttribute("Error", "No hay suficiente cantidad en Inventario para el producto "+ producto.getNombreProducto());
            }
        });
        return "redirect:/realizarVenta";
    }

    //Listar todas las ventas realizadas
    @GetMapping("/detalles")
    public String listarVentas(Model model) {
        List<Venta> ventas = ventaServicio.obtenerTodasLasVentas();

        //Formatear las fechas
        ventas.forEach(venta ->{
            if (venta.getFechaVenta() != null){
                DateTimeFormatter formatear = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                venta.setFechaFormateada(venta.getFechaVenta().format(formatear));
            }
        });

        model.addAttribute("ventas", ventas);
        return "listadoVentas";//vista de listado de ventas
    }


    @PostMapping("/guardar")
    public ResponseEntity<Map<String, String>> guardarVenta (@RequestBody VentaRequest ventaRequest){
        //Aqui se realiza el proceso de guardado
        ventaServicio.guardarVenta(ventaRequest);

        //Devuelve una respuesta JSON con un mensaje
        Map<String, String> response = new HashMap<>();
        response.put("message", "Venta registrada con éxito");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public String verDetallesVenta(@PathVariable Long id, Model model){
        Venta venta = ventaServicio.obtenerVentaPorId(id);

        if (venta.getFechaVenta() !=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String fechaFormateada = venta.getFechaVenta().format(formatter);
            model.addAttribute("fechaFormateada", fechaFormateada);
        }

        model.addAttribute("venta", venta);
        return "detallesVentaId";
    }



}
