package mq.inventario.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class VentaDTO {
    private Map<Long,ProductoVentaDTO> detallesVenta = new HashMap<>();//Inicializacion en el campo
    private List<DetalleVentaDTO> producto;
    public VentaDTO() {
        this.detallesVenta = new HashMap<>();
    }
}
