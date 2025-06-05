package com.hd.GestionTareas.curso.repository;

import com.hd.GestionTareas.user.repository.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "curso_docente",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "docente_id")
    )
    private Set<User> docentesAsignados = new HashSet<>();

    public void agregarDocente(User docente) {
        if (docente != null) {
            if (this.docentesAsignados == null) {
                this.docentesAsignados = new HashSet<>();
            }
            this.docentesAsignados.add(docente);
            if (docente.getCursosAsignados() == null) {
                docente.setCursosAsignados(new HashSet<>());
            }
            docente.getCursosAsignados().add(this);
        }
    }

    public void removerDocente(User docente) {
        if (docente != null && this.docentesAsignados != null) {
            this.docentesAsignados.remove(docente);
            if (docente.getCursosAsignados() != null) {
                docente.getCursosAsignados().remove(this);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return Objects.equals(id, curso.id) &&
               Objects.equals(codigo, curso.codigo) &&
               Objects.equals(nombre, curso.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo, nombre);
    }
}
