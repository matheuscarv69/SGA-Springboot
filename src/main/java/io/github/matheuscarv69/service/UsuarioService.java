package io.github.matheuscarv69.service;

import io.github.matheuscarv69.domain.entity.Usuario;
import io.github.matheuscarv69.rest.dto.*;

import java.util.List;
public interface UsuarioService {

    Usuario salvar(Usuario usuario);

    void atualizarUser(Integer id, Usuario usuario);

    void desligarUser(Integer id);

    void ativarUser(Integer id);

    Usuario buscarUserPorId(Integer id);

    List<Usuario> buscarTecnicos();

    List<Usuario> buscarAdministradores();

    void setTecn(Integer id, BoolTecnicoDTO boolTecnDTO);

    void setAdmin(Integer id, BoolAdministradorDTO admin);

    List<InformacoesChamadoDTO> buscarChamadosReq(Integer id);

    List<InformacoesChamadoDTO> buscarChamadosTecn(Integer id);

    List<Usuario> buscarPorPar(Usuario filtro);

    TokenDTO autenticar(CredenciaisDTO credenciais);

}
