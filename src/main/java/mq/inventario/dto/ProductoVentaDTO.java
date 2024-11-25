package mq.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoVentaDTO extends DetalleVentaDTO {
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private Double precio;
}
