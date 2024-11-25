package mq.inventario.dto;

import lombok.Data;

import java.util.List;

@Data

public class VentaRequest {

    private List<DetalleVenta> productos;
    private double total;

    @Data
    public static class DetalleVenta {
        private String nombre;
        private int cantidad;
        private double precio;


    }

}
