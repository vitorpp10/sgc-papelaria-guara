package br.com.sgc;

import br.com.sgc.domain.model.Usuario;
import br.com.sgc.domain.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Se não existir o usuário admin no banco, cria ele automaticamente na inicialização
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setSenha(passwordEncoder.encode("senha123")); // Senha criptografada
            admin.setPerfil(Usuario.Perfil.ADMIN);
            
            usuarioRepository.save(admin);
            System.out.println("====== USUÁRIO DE TESTE CRIADO: admin / senha123 ======");
        }
    }
}
