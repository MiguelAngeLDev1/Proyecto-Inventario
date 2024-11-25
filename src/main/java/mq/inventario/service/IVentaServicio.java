package mq.inventario.service;

import mq.inventario.models.Venta;

import java.util.List;

public interface IVentaServicio {

    public Venta realizarVenta(List<Long> productosIds);

    List<Venta> obtenerTodasLasVentas();

    Venta obtenerVentaPorId(Long id);

}
