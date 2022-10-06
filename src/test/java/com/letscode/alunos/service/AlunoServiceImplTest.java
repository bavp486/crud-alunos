package com.letscode.alunos.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.letscode.alunos.entity.Aluno;
import com.letscode.alunos.repository.AlunoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceImplTest {

    /*
    Testar criacao de aluno - feito
    Testar busca por id - feito
    Testar uma lista de alunos - feito
    Testar delete
    Testar atualizacao
    Testar excecao caso o aluno na exista - feito
     */

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoServiceImpl alunoService;

    Aluno aluno;

    @BeforeEach
    void setup() {
        aluno = new Aluno(1L, "Aluno Teste",
                18L, "436556565", "Rua teste");
    }

    @Test
    @DisplayName("Teste criacao do aluno")
    void testaCriacaoDoAluno() {
        //AAA
        // Inicio o que é necessario para testar

        // Simula o comportamento
        Mockito.when(alunoRepository.save(aluno)).thenReturn(aluno);

        // Chamo o metodo que vai ser testado
        var resultado = alunoService.salvar(aluno);

        // Verica o resultado
        assertEquals(aluno, resultado);
    }

    @Test
    @DisplayName("Deve buscar aluno pelo id")
    void deveBuscarAlunoPeloId() throws Exception {
        //Simulou o comportamento do repository
        Mockito.when(alunoRepository
                .findById(anyLong())).thenReturn(Optional.of(aluno));

        // Chama o metodo que vai ser executado
        var resultado = alunoService.buscaPorId(aluno.getId());

        // Validacao
        assertEquals(aluno, resultado);
    }

    @Test
    @DisplayName("Deve retornar um excecao quando busca por aluno retorna null")
    void deveRetornarUmExcecaoQuandoBuscaPorAlunoRetornaNull() {
        //Simulamos o comportamento de erro
        Mockito.when(alunoRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //Testando excecao
        Exception exception = assertThrows(Exception.class,
                () -> alunoService.buscaPorId(aluno.getId()));

        //Validacao
        assertEquals("Aluno não foi encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma lista de alunos")
    void deveRetornarUmaListaDeAlunos() {
        //Mockar
        List<Aluno> alunos = Arrays.asList(aluno);

        Mockito.when(alunoRepository.findAll()).thenReturn(alunos);

        var resultado = alunoService.buscaTodos();

        // assertAll vs any assertEquals
        Assertions.assertAll(
                () -> Assertions.assertEquals(alunos, resultado),
                () -> Assertions.assertEquals(1, resultado.size()), //falha
                () -> Assertions.assertEquals("Aluno Teste", resultado.get(0).getNome()) //terceiro
        );
//        Assertions.assertEquals(alunos, resultado); //faz o primeiro
//        Assertions.assertEquals(2, resultado.size()); // falha
//        Assertions.assertEquals("Aluno Teste", resultado.get(0).getNome()); // nunca executa
    }

    @Test
    @DisplayName("Deve deletar um aluno")
    void deveDeletarUmAluno() throws Exception {

//        Mockito.doReturn(aluno).when(alunoRepository.findById(anyLong()));

//        BDDMockito.given(alunoRepository.findById(anyLong())).willReturn(Optional.of(aluno));
//        BDDMockito.then(alunoRepository.findById(any())).should().get()

        Mockito.when(alunoRepository.findById(anyLong()))
                .thenReturn(Optional.of(aluno));

        Mockito.doNothing().when(alunoRepository).deleteById(anyLong());

        var resultado = alunoService.delete(aluno.getId());

        Assertions.assertEquals("Aluno deletado", resultado);
    }

    @Test
    @DisplayName("Deve alterar o nome do aluno")
    void deveAlterarONomeDoAluno() throws Exception {

        Mockito.when(alunoRepository.findById(anyLong()))
                .thenReturn(Optional.of(aluno));

        Mockito.when(alunoRepository.save(any())).thenReturn(aluno);

        var resultado = alunoService.alterarAluno(aluno.getId(), "Superman");

        Assertions.assertEquals("Superman", resultado.getNome());
    }


    @Test
    @DisplayName("Deve retornar excessao quando aluno nao for encontrado para delecao")
    void deveRetornarExcessaoQuandoAlunoNaoForEncontradoParaDelecao() {
        Exception exception = assertThrows(Exception.class,
                () -> alunoService.delete(aluno.getId()));

        Assertions.assertEquals("Aluno não foi encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar excessao quando aluno nao for encontrado para alteracao")
    void deveRetornarExcessaoQuandoAlunoNaoForEncontradoParaAlteracao() {
        Exception exception = assertThrows(Exception.class,
                () -> alunoService.alterarAluno(aluno.getId(), "Batman"));

        Assertions.assertEquals("Aluno não foi encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve testar a busca por nome")
    void deveTestarABuscaPorNome() {
        Mockito.when(alunoRepository.findByNome(anyString())).thenReturn(List.of(aluno));

        List<Aluno> alunos = alunoService.buscaPorNome(aluno.getNome());
        Assertions.assertEquals("Aluno Teste", alunos.get(0).getNome());
    }

    @Test
    @DisplayName("Deve buscar o aluno somento pelo nome")
    void deveBuscarOAlunoSomentoPeloNome() {
        Mockito.when(alunoRepository.findByNome(anyString())).thenReturn(List.of(aluno));
        List<Aluno> alunos = alunoService.filter(aluno.getNome(), null, null);
        Assertions.assertEquals("Aluno Teste", alunos.get(0).getNome());
    }

    @Test
    @DisplayName("Deve buscar uma lista de alunos pela idade")
    public void deveBuscarUmaListaDeAlunosPelaIdade(){
        Mockito.when(alunoRepository.findAllByIdade(anyLong())).thenReturn(List.of(aluno));
        List<Aluno>alunos = alunoService.buscaPorIdade(aluno.getIdade());

        Assertions.assertAll(
                () -> Assertions.assertEquals("Aluno Teste", alunos.get(0).getNome()),
                () -> Assertions.assertEquals( aluno,alunos.get(0))
        );
    }

    @Test
    @DisplayName("Deve buscar o aluno somente pela idade")
    void deveBuscarOAlunoSomentePeloIdade() {
        Mockito.when(alunoRepository.findAllByIdade(anyLong())).thenReturn(List.of(aluno));
        List<Aluno> alunos = alunoService.filter(null, aluno.getIdade(), null);
        Assertions.assertEquals("Aluno Teste", alunos.get(0).getNome());
    }

    @Test
    @DisplayName("Deve buscar o aluno que nao existe")
    void deveBuscarOAlunoQueNaoExiste() {
      //  Mockito.when(alunoRepository.findAllByIdade(anyLong())).thenReturn(null); Não precisa de mockar pois o codigo não passa no repositorio
        List<Aluno> alunos = alunoService.filter(any(), any(), any());
        Assertions.assertEquals(0,alunos.size());
    }

    @Test
    @DisplayName("Deve buscar o aluno que existe por todos os dados")
    void deveBuscarOAlunoQueExistePorTodosOsDados() {
        Mockito.when(alunoRepository.findByNomeAndIdadeAndDocumento(any(),any(),any())).thenReturn(Optional.of(aluno));
        List<Aluno> alunos = alunoService.filter(aluno.getNome(), aluno.getIdade(), aluno.getDocumento());
        Assertions.assertEquals(aluno,alunos.get(0));
    }


}