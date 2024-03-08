package testes;

import dao.AlunoDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import modelo.Aluno;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import util.JPAUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        EntityManager em = JPAUtil.getEntityManager();
        Scanner leitorTeclado = new Scanner(System.in);
        AlunoDao alunoDao = new AlunoDao(em);
        List<Aluno> alunosCadastrados = new ArrayList<>();

        em.getTransaction().begin();

        int opcao;
        do {
            System.out.println("Menu:");
            System.out.println("1- Cadastrar aluno");
            System.out.println("2- Excluir aluno");
            System.out.println("3- Alterar aluno");
            System.out.println("4- Buscar pelo nome");
            System.out.println("5- Listar alunos com status");
            System.out.println("6- Sair");
            System.out.print("Escolha uma opção: ");
            opcao = leitorTeclado.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("Digite o nome do aluno: ");
                    String nome = leitorTeclado.next();

                    boolean nomeExistente = alunosCadastrados.stream().anyMatch(a -> a.getNome().equals(nome));
                    if (nomeExistente) {
                        System.out.println("Já existe um aluno cadastrado com esse nome!");
                        break;
                    }

                    System.out.println("Digite o RA do aluno: ");
                    String ra = leitorTeclado.next();
                    System.out.println("Digite o email do aluno: ");
                    String email = leitorTeclado.next();
                    System.out.println("Digite a nota 1: ");
                    BigDecimal nota1 = leitorTeclado.nextBigDecimal();
                    System.out.println("Digite a nota 2: ");
                    BigDecimal nota2 = leitorTeclado.nextBigDecimal();
                    System.out.println("Digite a nota 3: ");
                    BigDecimal nota3 = leitorTeclado.nextBigDecimal();

                    Aluno novoALuno = new Aluno(nome, ra, email, nota1, nota2, nota3);
                    alunoDao.cadastrarAluno(novoALuno);
                    alunosCadastrados.add(novoALuno);
                    System.out.println("Aluno cadastrado com sucesso!");
                    break;
                case 2:
                    System.out.println("Digite o nome do aluno que deseja excluir:");
                    String nomeExcluir = leitorTeclado.next();

                    boolean encontradoExcluir = alunosCadastrados.stream().anyMatch(a -> a.getNome().equals(nomeExcluir));
                    if (!encontradoExcluir) {
                        System.out.println("Aluno não encontrado!");
                        break;
                    }
                    alunoDao.excluirPeloNome(nomeExcluir);
                    System.out.println("Aluno excluído com sucesso!");
                    break;
                case 3:
                    System.out.println("Digite o nome do aluno que você deseja alterar: ");
                    String nomeAlterar = leitorTeclado.next();
                    boolean encontrado = false;
                    for (Aluno aluno : alunosCadastrados) {
                        if (aluno.getNome().equals(nomeAlterar)) {
                            encontrado = true;
                            System.out.println("Aluno encontrado!");
                            System.out.println("Digite o novo nome: ");
                            String novoNome = leitorTeclado.next();
                            System.out.println("Digite o novo RA: ");
                            String novoRA =  leitorTeclado.next();
                            System.out.println("Digite o novo email: ");
                            String novoEmail = leitorTeclado.next();
                            System.out.println("Digite a nova nota 1");
                            BigDecimal novaNota1 = leitorTeclado.nextBigDecimal();
                            System.out.println("Digite a nova nota 2");
                            BigDecimal novaNota2 = leitorTeclado.nextBigDecimal();
                            System.out.println("Digite a nova nota 3");
                            BigDecimal novaNota3 = leitorTeclado.nextBigDecimal();
                            aluno.setNome(novoNome);
                            alunoDao.alterarPeloNome(nomeAlterar, novoNome, novoRA, novoEmail, novaNota1, novaNota2, novaNota3);
                            System.out.println("Aluno alterado com sucesso!");
                            break;
                        }
                    }
                    if (!encontrado) {
                        System.out.println("Aluno não encontrado!");
                    }
                    break;
                case 4:
                    System.out.println("Digite o nome do aluno que deseja buscar:");
                    String nomeBuscar = leitorTeclado.next();

                    try {
                        Aluno alunoBusca = alunoDao.buscarPeloNome(nomeBuscar);
                        System.out.println("Aluno encontrado:");
                        System.out.println("Nome: " + alunoBusca.getNome());
                        System.out.println("RA: " + alunoBusca.getRa());
                        System.out.println("Email: " + alunoBusca.getEmail());
                        System.out.println("Nota 1: " + alunoBusca.getNota1());
                        System.out.println("Nota 2: " + alunoBusca.getNota2());
                        System.out.println("Nota 3: " + alunoBusca.getNota3());
                    } catch (NoResultException e) {
                        System.out.println("Aluno não encontrado!");
                    }
                    break;

                case 5:
                    List<Object[]> alunosStatus = alunoDao.listarAlunosComSituacao();
                    for (Object[] aluno : alunosStatus) {
                        System.out.println("Nome: " + aluno[0]);
                        System.out.println("Email: " + aluno[1]);
                        System.out.println("RA: " + aluno[2]);
                        System.out.println("Nota 1: " + aluno[3]);
                        System.out.println("Nota 2: " + aluno[4]);
                        System.out.println("Nota 3: " + aluno[5]);
                        System.out.println("Média: " + aluno[6]);
                        System.out.println("Situação: " + aluno[7]);
                    }
                    break;
                case 6:
                    System.out.println("Saindo do programa...");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        } while (opcao != 6);

        em.getTransaction().commit();
        em.close();
        leitorTeclado.close();
    }
}