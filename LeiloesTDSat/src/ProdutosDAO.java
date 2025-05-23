/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class ProdutosDAO {
    
    public void cadastrarProduto(ProdutosDTO produto){
        Connection conn = new conectaDAO().connectDB();
        
        try {
            String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getValor());
            stmt.setString(3, produto.getStatus());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + erro.getMessage());
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutos(){
    Connection conn = new conectaDAO().connectDB();
    ArrayList<ProdutosDTO> lista = new ArrayList<>();
    
    try {
        String sql = "SELECT * FROM produtos WHERE status = 'A Venda'"; // Alteração aqui
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            ProdutosDTO produto = new ProdutosDTO();
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setValor(rs.getInt("valor"));
            produto.setStatus(rs.getString("status"));
            lista.add(produto);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage());
    }
    return lista;
}
    
    public boolean venderProduto(int id) {
    Connection conn = new conectaDAO().connectDB();
    PreparedStatement stmt = null;
    try {
        String checkSql = "SELECT status FROM produtos WHERE id = ?";
        stmt = conn.prepareStatement(checkSql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        if (!rs.isBeforeFirst()) {
            JOptionPane.showMessageDialog(null, "ID não encontrado!");
            return false;
        }
        
        rs.next();
        if (rs.getString("status").equals("Vendido")) {
            JOptionPane.showMessageDialog(null, "Produto já foi vendido!");
            return false;
        }

        String updateSql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
        stmt = conn.prepareStatement(updateSql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        
        return true;
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro ao vender produto: " + e.getMessage());
        return false;
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
    
    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
    Connection conn = new conectaDAO().connectDB();
    ArrayList<ProdutosDTO> lista = new ArrayList<>();
    
    try {
        String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            ProdutosDTO produto = new ProdutosDTO();
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setValor(rs.getInt("valor"));
            produto.setStatus(rs.getString("status"));
            lista.add(produto);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro ao listar produtos vendidos: " + e.getMessage());
    }
    return lista;
}
}

