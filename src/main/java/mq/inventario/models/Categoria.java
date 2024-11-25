package mq.inventario.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categorias")
@ToString(exclude = "productos") // Excluye la lista de productos para evitar ciclos
@EqualsAndHashCode(of = "idCategoria") // Solo usa el id para evitar ciclos
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idCategoria;

    @NotBlank(message = "El nombre no debe ir vacio")
    @Size(max = 20, message = "El nombre no puede exceder los 50 caracteres")
    private String nombreCategorial;

    @OneToMany(mappedBy = "categoria")//relacion con la clase producto
    private List<Producto> productos;
}
