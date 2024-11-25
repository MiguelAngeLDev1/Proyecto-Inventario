package mq.inventario.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productos")
@ToString(exclude = "categoria")// Excluye la relación para evitar ciclos
@EqualsAndHashCode(of = "id") // Solo usa el id para evitar ciclos
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no acepta valores vacios")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String nombreProducto;

    private String descripcionProducto;

    @NotNull(message = "El precio no debe ir vacio")
    private Double precioProducto;

    private int cantidadProducto;

    //Relacion de muchos a uno con categoria
    @ManyToOne
    @JoinColumn(name = "categoria_id")//clave foranea
    private Categoria categoria;

}
