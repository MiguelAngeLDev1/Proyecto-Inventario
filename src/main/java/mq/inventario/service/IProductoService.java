package mq.inventario.service;

import mq.inventario.models.Producto;

import java.util.List;

public interface IProductoService {

    List<Producto> listar();

    Producto obtenerProductoPorId(Long id);

    Producto agregarProducto(Producto producto);

    Producto actualizarProducto(Producto producto);

    void eliminarProducto(Long id);
}
