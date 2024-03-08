package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import modelo.Aluno;

import java.math.BigDecimal;
import java.util.List;

public class AlunoDao {
    private EntityManager em;
    public AlunoDao(EntityManager em) {
        this.em = em;
    }

    public void cadastrarAluno(Aluno aluno) {
        this.em.persist(aluno);
    }

    public void excluirPeloNome(String nome) {
        String jpql = "DELETE FROM Aluno a WHERE a.nome = :nome";
        em.createQuery(jpql)
                .setParameter("nome", nome)
                .executeUpdate();
    }


    public void alterarPeloNome(String nome, String novoNome, String novoRa, String novoEmail, BigDecimal novaNota1, BigDecimal novaNota2, BigDecimal novaNota3) {
        String jpql = "UPDATE Aluno a SET a.nome = :novoNome, a.ra = :novoRa, a.email = :novoEmail, a.nota1 = :novaNota1, a.nota2 = :novaNota2, a.nota3 = :novaNota3 WHERE a.nome = :nome";
        em.createQuery(jpql)
                .setParameter("novoNome", novoNome)
                .setParameter("novoRa", novoRa)
                .setParameter("novoEmail", novoEmail)
                .setParameter("novaNota1", novaNota1)
                .setParameter("novaNota2", novaNota2)
                .setParameter("novaNota3", novaNota3)
                .setParameter("nome", nome)
                .executeUpdate();
    }

    public Aluno buscarPeloNome(String nome) {
        String jpql = "SELECT a FROM Aluno a WHERE a.nome = :n";
        return em.createQuery(jpql, Aluno.class)
                .setParameter("n", nome)
                .getSingleResult();
    }





    /*public List<Aluno> listarAlunosComSituacao() {
        String jpql = "SELECT a FROM Aluno a";
        List<Aluno> alunos = em.createQuery(jpql, Aluno.class).getResultList();

        for (Aluno aluno : alunos) {
            BigDecimal media = (aluno.getNota1().add(aluno.getNota2()).add(aluno.getNota3())).divide(BigDecimal.valueOf(3));
            String situacao;
            if (media.compareTo(BigDecimal.valueOf(6)) >= 0) {
                situacao = "Aprovado";
            } else if (media.compareTo(BigDecimal.valueOf(4)) >= 0) {
                situacao = "Recuperação";
            } else {
                situacao = "Reprovado";
            }
            aluno.setSituacao(situacao);
        }
        return alunos;
    }*/


    public List<Object[]> listarAlunosComSituacao() {
        String jpql = "SELECT a.nome, a.email, a.ra, a.nota1, a.nota2, a.nota3, " +
                "TRUNC((a.nota1 + a.nota2 + a.nota3) / 3, 2) AS media, " +
                "CASE " +
                "   WHEN ((a.nota1 + a.nota2 + a.nota3) / 3) < 4 THEN 'Reprovado' " +
                "   WHEN ((a.nota1 + a.nota2 + a.nota3) / 3) >= 4 AND ((a.nota1 + a.nota2 + a.nota3) / 3) < 6 THEN 'Recuperação' " +
                "   ELSE 'Aprovado' " +
                "END AS situacao " +
                "FROM Aluno a";

        return em.createQuery(jpql).getResultList();
    }

}
