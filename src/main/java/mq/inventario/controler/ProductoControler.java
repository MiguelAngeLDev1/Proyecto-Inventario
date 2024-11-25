package mq.inventario.controler;

import jakarta.persistence.Id;
import mq.inventario.models.Categoria;
import mq.inventario.models.Producto;
import mq.inventario.service.CategoriaService;
import mq.inventario.service.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductoControler {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoServicio productoServicio;

    @GetMapping("/")
    public String inicio(Model model) {
        return "index";
    }

    //metodo para mostrar el formulario
    @GetMapping("/agregar")
    public String agregar(Model model) {
        //se crea un objeto producto para el formulario
        Producto producto = new Producto();
        model.addAttribute("producto", producto);

        //obtenemos todas las categorias
        List<Categoria>categorias = categoriaService.obtenerCategorias();
        model.addAttribute("categorias", categorias);
        return "agregar";
    }

    //metodo para enviar datos a la base de datos
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("producto")Producto producto, Model model, BindingResult result) {
        //Verifica si hay errores de validacion
        if (result.hasErrors()) {
            //si hay errires, volver a cargar el formulario con la lista de categorias
            List<Categoria>categorias = categoriaService.obtenerCategorias();
            model.addAttribute("categorias", categorias);
            return "agregar";
        }

        //Guardar el producto en la base de datos usando el servicio
        productoServicio.agregarProducto(producto);

        //Redirigimos al listado de productos
        return "redirect:/";
    }

    //metodo mostrar tabla de productos
    @GetMapping("/productos")
    public String productos(Model model) {
        model.addAttribute("productos", productoServicio.listar());
        return "listaProductos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        //Busca el producto por el id
        Producto producto = productoServicio.obtenerProductoPorId(id);
        if (producto.getCategoria() == null) {
            producto.setCategoria(new Categoria());  // Asignar una instancia vacía si es nulo
        }
        List<Categoria>categorias = categoriaService.obtenerCategorias();
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categorias);
        return "editar";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute("producto")Producto producto) {
        productoServicio.actualizarProducto(producto); //Se llama al servicio de actualizacion
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id) {
        Producto producto = productoServicio.obtenerProductoPorId(id);
        if(producto != null){
            productoServicio.eliminarProducto(id);
        }else {
            return "redirect:/";
        }
        return "redirect:/productos";
    }

}
