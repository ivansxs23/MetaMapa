package ar.edu.utn.frba.dds.dtos;

import ar.edu.utn.frba.dds.models.entities.Permiso;
import ar.edu.utn.frba.dds.models.entities.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesPermissionsDTO {
    private String username;
    private Rol rol;
    private List<Permiso> permisos;
}
