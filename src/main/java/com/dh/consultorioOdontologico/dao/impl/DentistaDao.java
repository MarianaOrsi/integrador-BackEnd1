package com.dh.consultorioOdontologico.dao.impl;

import com.dh.consultorioOdontologico.dao.IDao;
import com.dh.consultorioOdontologico.dao.configuracaoJDBC.ConfiguracaoJDBC;
import com.dh.consultorioOdontologico.model.Dentista;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DentistaDao implements IDao<Dentista> {

    static final Logger logger = Logger.getLogger(DentistaDao.class);
    private ConfiguracaoJDBC configuracaoJDBC = new ConfiguracaoJDBC();
    private List<Dentista> listaDentistas = new ArrayList<>();

    @Override
    public Dentista cadastrar(Dentista dentista) throws SQLException {
        String DENTISTA_INSERT = String.format("INSERT INTO Dentista(nome, sobrenome, matricula) VALUES('%s', '%s', '%s')", dentista.getNome(), dentista.getSobrenome(), dentista.getMatricula());
        Connection connection = null;
        try{
            logger.info("Abrindo conexão com o banco.");
            connection = configuracaoJDBC.getConnectionH2();
            Statement statement = connection.createStatement();
            statement.execute(DENTISTA_INSERT, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                dentista.setId(resultSet.getInt("id"));
            }
            logger.info("Dentista " + dentista.getNome() + " salvo no banco.");
        } catch (Exception e){
            logger.error("Erro no cadastro do dentista.");
            e.printStackTrace();

        } finally {
            logger.info("Fechando conexão com o banco de dados.");
            if(connection!=null) connection.close();
        }
        return dentista;
    }

    @Override
    public Dentista modificar(Dentista dentista) throws SQLException {
        return null;
    }

    @Override
    public void excluir(Dentista dentista) throws SQLException{
        String SQL_DELETE_DENTISTA = String.format("DELETE FROM Dentista WHERE matricula = '%d'", dentista.getMatricula());
        Connection connection = null;

        try{
            logger.info("Abrindo conexão com o banco para excluir um dentista.");
            connection = configuracaoJDBC.getConnectionH2();
            Statement statement = connection.createStatement();
            statement.execute(SQL_DELETE_DENTISTA);
            System.out.println("Dentista exclído com sucesso!");
            logger.info("Dentista excluído do banco.");
        }catch (Exception e){
            logger.error("Erro ao excluir dentista do banco.");
            e.printStackTrace();
        }finally {
            connection.close();
            logger.info("Conexão com o banco encerrada.");
        }
    }

    public List<Dentista> buscarTodos() throws SQLException{
        String BUSCAR_DENTISTAS = String.format("SELECT * FROM DENTISTA;");
        Connection connection = null;
        try{
            logger.info("Abrindo conexão com o banco para trazer lista de dentistas cadastrados.");
            connection = configuracaoJDBC.getConnectionH2();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(BUSCAR_DENTISTAS);
            while (resultSet.next()){
                Dentista dentista = new Dentista(resultSet.getInt("id"), resultSet.getString("nome"),
                        resultSet.getString("sobrenome"), resultSet.getInt("matricula"));
                listaDentistas.add(dentista);
            }
        } catch (Exception e){
            logger.warn("Erro de conexão com o banco ao executar listagem de dentistas.");
            e.printStackTrace();
        } finally {
            logger.info("Fechando conexão com o banco de dados.");
            connection.close();
            System.out.println(listaDentistas);
            return listaDentistas;
        }
    }
}
