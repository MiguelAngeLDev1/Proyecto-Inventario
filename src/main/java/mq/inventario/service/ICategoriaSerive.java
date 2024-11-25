package mq.inventario.service;

import mq.inventario.models.Categoria;

import java.util.List;

public interface ICategoriaSerive {

    List<Categoria> obtenerCategorias();
    Categoria obtenerCategoriaPorId(Long id);
    Categoria guardarCategoria(Categoria categoria);
    Categoria actualizarCategoria(Long id,Categoria categoria);
    void eliminarCategoria(Long id);


}
