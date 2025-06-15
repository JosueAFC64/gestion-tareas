package com.hd.GestionTareas.recursoseducativos.repository;

import com.hd.GestionTareas.TipoRecurso;
import com.hd.GestionTareas.curso.repository.Curso;
import com.hd.GestionTareas.user.repository.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recursos_educativos")
public class RecursosEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoRecurso tipo; // DOCUMENTO, VIDEO, ENLACE, EJERCICIO

    @Column(nullable = false)
    private String url;

    @Column(name = "google_drive_file_id")
    private String googleDriveFileId;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @Column
    @UpdateTimestamp
    private LocalDateTime fechaUltimaModificacion;

    // Relación con el docente que creó el recurso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id")
    private User creador;

    // Relación con el curso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

}
