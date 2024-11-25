package mq.inventario.controler;

import mq.inventario.models.Categoria;
import mq.inventario.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/categorias")
public class CategoriaControler {

    @Autowired
    private CategoriaService categoriaService;

    //Mostrar el formulario para agregar una nueva categoria
    @GetMapping("/nuevo")
    public String mostrarFormularioCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());//Crea un objeto vacio de Categoria
        return "categoria_form";
    }

    //Manejar la solicitud POST para guardar la categoria
    @PostMapping
    public String guardarCategoria(@ModelAttribute("categoria") Categoria categoria) {
        categoriaService.guardarCategoria(categoria);
        return "redirect:/categorias";//Redirigir a la pagina de listado
    }

    //Listar las categorias
    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.obtenerCategorias());
        return "categorias";
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id){
        categoriaService.eliminarCategoria(id);
    }
}
