package ar.edu.utn.frba.dds.service.impl;

import ar.edu.utn.frba.dds.models.entities.Usuario;
import ar.edu.utn.frba.dds.models.repositories.UsuariosRepository;

public class UsuarioService {
    private final UsuariosRepository usuariosRepository;

    public UsuarioService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    public Usuario agregarUsuario(Usuario usuario) {
        return usuariosRepository.save(usuario);
    }


}
