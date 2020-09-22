package com.rvleustaquio.dbmigra;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import com.rvleustaquio.dbmigra.enums.FontDad;
import com.rvleustaquio.dbmigra.model.Coluna;
import com.rvleustaquio.dbmigra.model.Direcao;
import com.rvleustaquio.dbmigra.model.Tabela;
import com.rvleustaquio.dbmigra.utils.Util;

public class App extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private Connection connSQLServer;
    private Connection connSyBase9;
    private SwingWorker<String, Object> swMig;
    private JPanel pnlOpened;
    private Direcao direcao;

    // region Components Declarations
    private JPanel pnlNav;
    private JPanel pnlFuncs;
    private JPanel pnlInicio;
    private JPanel pnlSQLServer;
    private JPanel pnlSyBase9;
    private JPanel pnlTabelas;
    private JPanel pnlMigracao;

    private JToggleButton tgbInicio;
    private JToggleButton tgbSQLServer;
    private JToggleButton tgbSyBase9;
    private JToggleButton tgbTabelas;
    private JToggleButton tgbMigracao;

    private JButton btnSair;
    private JButton btnAvancar;
    private JButton btnVoltar;
    private JButton btnSQLServerAtualizarBanco;
    private JButton btnSQLServerTestarConexao;
    private JButton btnSyBase9TestarConexao;
    private JButton btnSQLServerAdicionaTodos;
    private JButton btnSQLServerAdicionaUm;
    private JButton btnSyBase9RemoveTodos;
    private JButton btnSyBase9RemoveUm;

    private JLabel lblInicio;
    private JLabel lblConfiguracoes;
    private JLabel lblPrefixo;
    private JLabel lblFiltro;
    private JLabel lblLinhasBatch;
    private JLabel lblDirecao;
    private JLabel lblSQLServer;
    private JLabel lblSQLServerHost;
    private JLabel lblSQLServerUsuario;
    private JLabel lblSQLServerSenha;
    private JLabel lblSQLServerBanco;
    private JLabel lblSyBase9;
    private JLabel lblSyBase9Host;
    private JLabel lblSyBase9Servico;
    private JLabel lblSyBase9Usuario;
    private JLabel lblSyBase9Senha;
    private JLabel lblSQLServerTabelas;
    private JLabel lblSyBase9Tabelas;
    private JLabel lblMigracao;
    private JLabel lblQntTabelas;
    private JLabel lblQntLinhas;

    private JTextField txtPrefixo;
    private JTextField txtFiltro;
    private JTextField txtLinhasBatch;
    private JTextField txtSQLServerHost;
    private JTextField txtSQLServerUsuario;
    private JTextField txtSQLServerSenha;
    private JTextField txtSyBase9Host;
    private JTextField txtSyBase9Servico;
    private JTextField txtSyBase9Usuario;
    private JTextField txtSyBase9Senha;

    private JComboBox<String> cbxSQLServerBanco;
    private JComboBox<Direcao> cbxDirecao;

    private JSeparator sepConfiguracoes;
    private JSeparator sepSQLServer;
    private JSeparator sepSyBase9;

    private JScrollPane spSQLServer;
    private JScrollPane spSyBase9;
    private JScrollPane spMigracao;

    private JList<Tabela> lstSQLServer;
    private JList<Tabela> lstSyBase9;

    private JTextArea txaMigracao;

    private JProgressBar prbMigracaoLinhas;
    private JProgressBar prbMigracaoTabelas;
    // endregion

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                App window = new App();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public App() {
        initialize();
        showPanel(tgbInicio, pnlInicio);
    }

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }

    private void initialize() {
        setTitle("DBMIGRA");
        setResizable(false);
        setBounds(100, 100, 780, 490);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUIFont(new FontUIResource("Arial", Font.PLAIN, 12));
        getContentPane().setLayout(null);

        pnlNav = new JPanel();
        pnlNav.setBackground(Color.LIGHT_GRAY);
        pnlNav.setBounds(0, 0, 145, 461);
        pnlNav.setLayout(null);
        getContentPane().add(pnlNav);

        tgbInicio = new JToggleButton("Início");
        tgbInicio.addActionListener(e -> showPanel((JToggleButton) e.getSource(), pnlInicio));
        tgbInicio.setBounds(10, 11, 125, 23);
        pnlNav.add(tgbInicio);

        tgbSQLServer = new JToggleButton("SQL Server");
        tgbSQLServer.addActionListener(e -> showPanel((JToggleButton) e.getSource(), pnlSQLServer));
        tgbSQLServer.setBounds(10, 45, 125, 23);
        pnlNav.add(tgbSQLServer);

        tgbSyBase9 = new JToggleButton("SyBase 9");
        tgbSyBase9.addActionListener(e -> showPanel((JToggleButton) e.getSource(), pnlSyBase9));
        tgbSyBase9.setBounds(10, 79, 125, 23);
        pnlNav.add(tgbSyBase9);

        tgbTabelas = new JToggleButton("Tabelas");
        tgbTabelas.addActionListener(e -> showPanel((JToggleButton) e.getSource(), pnlTabelas));
        tgbTabelas.setBounds(10, 113, 125, 23);
        pnlNav.add(tgbTabelas);

        tgbMigracao = new JToggleButton("Migração");
        tgbMigracao.addActionListener(e -> showPanel((JToggleButton) e.getSource(), pnlMigracao));
        tgbMigracao.setBounds(10, 147, 125, 23);
        pnlNav.add(tgbMigracao);

        pnlFuncs = new JPanel();
        pnlFuncs.setBackground(Color.DARK_GRAY);
        pnlFuncs.setBounds(144, 414, 630, 47);
        pnlFuncs.setLayout(null);
        getContentPane().add(pnlFuncs);

        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> navTo("Volt"));
        btnVoltar.setBounds(333, 12, 89, 25);
        pnlFuncs.add(btnVoltar);

        btnAvancar = new JButton("Avançar");
        btnAvancar.addActionListener(e -> navTo("Avanc"));
        btnAvancar.setBounds(432, 12, 89, 25);
        pnlFuncs.add(btnAvancar);

        btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> System.exit(0));
        btnSair.setBounds(531, 12, 89, 25);
        pnlFuncs.add(btnSair);

        pnlInicio = new JPanel();
        pnlInicio.setBounds(144, 0, 630, 413);
        pnlInicio.setLayout(null);
        pnlInicio.setName("pnlInicio");
        getContentPane().add(pnlInicio);

        lblInicio = new JLabel("Migração de SQL Server para SyBase 9");
        lblInicio.setHorizontalAlignment(SwingConstants.CENTER);
        lblInicio.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblInicio.setBounds(10, 137, 610, 28);
        pnlInicio.add(lblInicio);

        lblConfiguracoes = new JLabel("Configurações");
        lblConfiguracoes.setHorizontalAlignment(SwingConstants.CENTER);
        lblConfiguracoes.setBounds(192, 176, 252, 14);
        pnlInicio.add(lblConfiguracoes);

        sepConfiguracoes = new JSeparator();
        sepConfiguracoes.setBounds(181, 201, 263, 2);
        pnlInicio.add(sepConfiguracoes);

        lblPrefixo = new JLabel("Prefixo:");
        lblPrefixo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPrefixo.setBounds(181, 217, 126, 14);
        pnlInicio.add(lblPrefixo);

        txtPrefixo = new JTextField("_mig");
        txtPrefixo.setBounds(317, 214, 127, 20);
        txtPrefixo.setColumns(10);
        pnlInicio.add(txtPrefixo);

        lblFiltro = new JLabel("Filtro:");
        lblFiltro.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFiltro.setBounds(181, 245, 126, 14);
        pnlInicio.add(lblFiltro);

        txtFiltro = new JTextField("_aud");
        txtFiltro.setBounds(317, 242, 127, 20);
        txtFiltro.setColumns(10);
        pnlInicio.add(txtFiltro);

        lblLinhasBatch = new JLabel("Regs/Pacote:");
        lblLinhasBatch.setHorizontalAlignment(SwingConstants.RIGHT);
        lblLinhasBatch.setBounds(181, 273, 126, 14);
        pnlInicio.add(lblLinhasBatch);

        txtLinhasBatch = new JTextField("5000");
        txtLinhasBatch.setBounds(317, 270, 127, 20);
        txtLinhasBatch.setColumns(10);
        pnlInicio.add(txtLinhasBatch);

        lblDirecao = new JLabel("Direção:");
        lblDirecao.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDirecao.setBounds(181, 301, 126, 14);
        pnlInicio.add(lblDirecao);

        cbxDirecao = new JComboBox<Direcao>();
        cbxDirecao.addItem(new Direcao(1, "SQL Server -> SyBase 9"));
        cbxDirecao.addItem(new Direcao(2, "SyBase 9 -> SQL Server"));
        cbxDirecao.addActionListener(this::setCbxDirecaoActionPerformed);
        cbxDirecao.setBounds(317, 298, 170, 20);
        pnlInicio.add(cbxDirecao);

        direcao = (Direcao) cbxDirecao.getSelectedItem();

        pnlSQLServer = new JPanel();
        pnlSQLServer.setBounds(144, 0, 630, 413);
        pnlSQLServer.setLayout(null);
        pnlSQLServer.setName("pnlSQLServer");
        getContentPane().add(pnlSQLServer);

        lblSQLServer = new JLabel("SQL Server");
        lblSQLServer.setBounds(10, 11, 113, 14);
        pnlSQLServer.add(lblSQLServer);

        lblSQLServerHost = new JLabel("Host:");
        lblSQLServerHost.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSQLServerHost.setBounds(155, 135, 132, 14);
        pnlSQLServer.add(lblSQLServerHost);

        txtSQLServerHost = new JTextField("127.0.0.1\\SQLEXPRESS");
        txtSQLServerHost.setBounds(297, 132, 200, 20);
        txtSQLServerHost.setColumns(10);
        pnlSQLServer.add(txtSQLServerHost);

        lblSQLServerUsuario = new JLabel("Usuário:");
        lblSQLServerUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSQLServerUsuario.setBounds(155, 163, 132, 14);
        pnlSQLServer.add(lblSQLServerUsuario);

        txtSQLServerUsuario = new JTextField("sa");
        txtSQLServerUsuario.setColumns(10);
        txtSQLServerUsuario.setBounds(297, 160, 200, 20);
        pnlSQLServer.add(txtSQLServerUsuario);

        lblSQLServerSenha = new JLabel("Senha:");
        lblSQLServerSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSQLServerSenha.setBounds(155, 191, 132, 14);
        pnlSQLServer.add(lblSQLServerSenha);

        txtSQLServerSenha = new JTextField("b5th4@custom");
        txtSQLServerSenha.setColumns(10);
        txtSQLServerSenha.setBounds(297, 188, 200, 20);
        pnlSQLServer.add(txtSQLServerSenha);

        lblSQLServerBanco = new JLabel("Banco de Dados:");
        lblSQLServerBanco.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSQLServerBanco.setBounds(155, 219, 132, 14);
        pnlSQLServer.add(lblSQLServerBanco);

        cbxSQLServerBanco = new JComboBox<>();
        cbxSQLServerBanco.setBounds(297, 217, 200, 20);
        pnlSQLServer.add(cbxSQLServerBanco);

        btnSQLServerAtualizarBanco = new JButton("...");
        btnSQLServerAtualizarBanco.addActionListener(this::btnSQLServerAtualizarBancoActionPerformed);
        btnSQLServerAtualizarBanco.setBounds(507, 217, 22, 20);
        pnlSQLServer.add(btnSQLServerAtualizarBanco);

        sepSQLServer = new JSeparator();
        sepSQLServer.setBounds(155, 248, 374, 2);
        pnlSQLServer.add(sepSQLServer);

        btnSQLServerTestarConexao = new JButton("Testar Conexão");
        btnSQLServerTestarConexao.addActionListener(e -> valSQLServerConexao(true, false));
        btnSQLServerTestarConexao.setBounds(397, 261, 132, 23);
        pnlSQLServer.add(btnSQLServerTestarConexao);

        pnlSyBase9 = new JPanel();
        pnlSyBase9.setBounds(144, 0, 630, 413);
        pnlSyBase9.setLayout(null);
        pnlSyBase9.setName("pnlSyBase9");
        getContentPane().add(pnlSyBase9);

        lblSyBase9 = new JLabel("SyBase 9");
        lblSyBase9.setBounds(10, 11, 113, 14);
        pnlSyBase9.add(lblSyBase9);

        lblSyBase9Host = new JLabel("Host:");
        lblSyBase9Host.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSyBase9Host.setBounds(155, 135, 132, 14);
        pnlSyBase9.add(lblSyBase9Host);

        txtSyBase9Host = new JTextField("127.0.0.1:6001");
        txtSyBase9Host.setBounds(297, 132, 200, 20);
        txtSyBase9Host.setColumns(10);
        pnlSyBase9.add(txtSyBase9Host);

        lblSyBase9Servico = new JLabel("Banco de Dados:");
        lblSyBase9Servico.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSyBase9Servico.setBounds(155, 163, 132, 14);
        pnlSyBase9.add(lblSyBase9Servico);

        txtSyBase9Servico = new JTextField("pmriofloresrj_trib_serv");
        txtSyBase9Servico.setColumns(10);
        txtSyBase9Servico.setBounds(297, 160, 200, 20);
        pnlSyBase9.add(txtSyBase9Servico);

        lblSyBase9Usuario = new JLabel("Usuário:");
        lblSyBase9Usuario.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSyBase9Usuario.setBounds(155, 192, 132, 14);
        pnlSyBase9.add(lblSyBase9Usuario);

        txtSyBase9Usuario = new JTextField("tecbth_rvleustaquio");
        txtSyBase9Usuario.setColumns(10);
        txtSyBase9Usuario.setBounds(297, 189, 200, 20);
        pnlSyBase9.add(txtSyBase9Usuario);

        lblSyBase9Senha = new JLabel("Senha:");
        lblSyBase9Senha.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSyBase9Senha.setBounds(155, 220, 132, 14);
        pnlSyBase9.add(lblSyBase9Senha);

        txtSyBase9Senha = new JTextField();
        txtSyBase9Senha.setColumns(10);
        txtSyBase9Senha.setBounds(297, 217, 200, 20);
        pnlSyBase9.add(txtSyBase9Senha);

        sepSyBase9 = new JSeparator();
        sepSyBase9.setBounds(155, 248, 374, 2);
        pnlSyBase9.add(sepSyBase9);

        btnSyBase9TestarConexao = new JButton("Testar Conexão");
        btnSyBase9TestarConexao.setBounds(397, 261, 132, 23);
        btnSyBase9TestarConexao.addActionListener(e -> valSyBase9Conexao(true));
        pnlSyBase9.add(btnSyBase9TestarConexao);

        pnlTabelas = new JPanel();
        pnlTabelas.setBounds(144, 0, 630, 413);
        pnlTabelas.setLayout(null);
        pnlTabelas.setName("pnlTabelas");
        getContentPane().add(pnlTabelas);

        lblSQLServerTabelas = new JLabel("Tabelas disponíveis");
        lblSQLServerTabelas.setBounds(10, 11, 173, 14);
        pnlTabelas.add(lblSQLServerTabelas);

        spSQLServer = new JScrollPane();
        spSQLServer.setBounds(10, 36, 300, 328);
        pnlTabelas.add(spSQLServer);

        lstSQLServer = new JList<>();
        spSQLServer.setViewportView(lstSQLServer);

        lblSyBase9Tabelas = new JLabel("Tabelas a serem migradas");
        lblSyBase9Tabelas.setBounds(320, 11, 173, 14);
        pnlTabelas.add(lblSyBase9Tabelas);

        spSyBase9 = new JScrollPane();
        spSyBase9.setBounds(320, 36, 300, 328);
        pnlTabelas.add(spSyBase9);

        lstSyBase9 = new JList<>();
        spSyBase9.setViewportView(lstSyBase9);

        btnSQLServerAdicionaUm = new JButton(">");
        btnSQLServerAdicionaUm.addActionListener(e -> {
            DefaultListModel<Tabela> tabsSyBase9 = (DefaultListModel<Tabela>) lstSyBase9.getModel();
            for (Tabela tabela : lstSQLServer.getSelectedValuesList()) {
                if (!tabsSyBase9.contains(tabela)) {
                    tabsSyBase9.addElement(tabela);
                }
            }
            lstSyBase9.setModel(tabsSyBase9);
        });
        btnSQLServerAdicionaUm.setBounds(199, 375, 50, 23);
        pnlTabelas.add(btnSQLServerAdicionaUm);

        btnSQLServerAdicionaTodos = new JButton(">>");
        btnSQLServerAdicionaTodos.addActionListener(e -> {
            DefaultListModel<Tabela> tabsSyBase9 = (DefaultListModel<Tabela>) lstSyBase9.getModel();
            ListModel<Tabela> tabsSQLServer = lstSQLServer.getModel();
            for (int i = 0; i < tabsSQLServer.getSize(); i++) {
                tabsSyBase9.addElement(tabsSQLServer.getElementAt(i));
            }
            lstSyBase9.setModel(tabsSyBase9);
        });
        btnSQLServerAdicionaTodos.setBounds(259, 375, 50, 23);
        pnlTabelas.add(btnSQLServerAdicionaTodos);

        btnSyBase9RemoveTodos = new JButton("<<");
        btnSyBase9RemoveTodos.addActionListener(e -> {
            DefaultListModel<Tabela> tabsSyBase9 = new DefaultListModel<>();
            lstSyBase9.setModel(tabsSyBase9);
        });
        btnSyBase9RemoveTodos.setBounds(320, 375, 50, 23);
        pnlTabelas.add(btnSyBase9RemoveTodos);

        btnSyBase9RemoveUm = new JButton("<");
        btnSyBase9RemoveUm.addActionListener(e -> {
            DefaultListModel<Tabela> tabsSyBase9 = (DefaultListModel<Tabela>) lstSyBase9.getModel();
            List<Tabela> tabsSyBase9Sel = lstSyBase9.getSelectedValuesList();
            for (Tabela tabela : tabsSyBase9Sel) {
                tabsSyBase9.removeElement(tabela);
            }
        });
        btnSyBase9RemoveUm.setBounds(380, 375, 50, 23);
        pnlTabelas.add(btnSyBase9RemoveUm);

        pnlMigracao = new JPanel();
        pnlMigracao.setBounds(144, 0, 630, 413);
        pnlMigracao.setLayout(null);
        pnlMigracao.setName("pnlMigracao");
        getContentPane().add(pnlMigracao);

        lblMigracao = new JLabel("Migração");
        lblMigracao.setBounds(10, 11, 133, 14);
        pnlMigracao.add(lblMigracao);

        spMigracao = new JScrollPane();
        spMigracao.setBounds(10, 36, 610, 316);
        pnlMigracao.add(spMigracao);

        txaMigracao = new JTextArea();
        spMigracao.setViewportView(txaMigracao);

        lblQntTabelas = new JLabel("Tabelas:");
        lblQntTabelas.setBounds(10, 362, 55, 14);
        lblQntTabelas.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlMigracao.add(lblQntTabelas);

        prbMigracaoTabelas = new JProgressBar();
        prbMigracaoTabelas.setBounds(75, 363, 540, 14);
        prbMigracaoTabelas.setStringPainted(true);
        pnlMigracao.add(prbMigracaoTabelas);

        lblQntLinhas = new JLabel("Linhas:");
        lblQntLinhas.setBounds(10, 387, 55, 14);
        lblQntLinhas.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlMigracao.add(lblQntLinhas);

        prbMigracaoLinhas = new JProgressBar();
        prbMigracaoLinhas.setBounds(75, 388, 540, 14);
        prbMigracaoLinhas.setStringPainted(true);
        pnlMigracao.add(prbMigracaoLinhas);
    }

    private boolean valSQLServer(boolean complete) {
        if (txtSQLServerHost.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o host!", "SQL Server",
                    JOptionPane.WARNING_MESSAGE);
            txtSQLServerHost.requestFocus();
            return false;
        }
        if (txtSQLServerUsuario.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o usuário!", "SQL Server",
                    JOptionPane.WARNING_MESSAGE);
            txtSQLServerUsuario.requestFocus();
            return false;
        }
        if (txtSQLServerSenha.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o senha!", "SQL Server",
                    JOptionPane.WARNING_MESSAGE);
            txtSQLServerSenha.requestFocus();
            return false;
        }
        if (complete) {
            if (cbxSQLServerBanco.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um banco de dados!", "SQL Server",
                        JOptionPane.WARNING_MESSAGE);
                cbxSQLServerBanco.requestFocus();
                return false;
            }
        }
        return true;
    }

    private boolean valSyBase9() {
        if (txtSyBase9Host.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o host!", "SyBase 9", JOptionPane.WARNING_MESSAGE);
            txtSyBase9Host.requestFocus();
            return false;
        }
        if (txtSyBase9Servico.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o serviço!", "SyBase 9",
                    JOptionPane.WARNING_MESSAGE);
            txtSyBase9Servico.requestFocus();
            return false;
        }
        if (txtSyBase9Usuario.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o usuário!", "SyBase 9",
                    JOptionPane.WARNING_MESSAGE);
            txtSyBase9Usuario.requestFocus();
            return false;
        }
        if (txtSyBase9Senha.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o senha!", "SyBase 9", JOptionPane.WARNING_MESSAGE);
            txtSyBase9Senha.requestFocus();
            return false;
        }
        return true;
    }

    private boolean valSQLServerConexao(boolean showMessage, boolean complete) {
        if (valSQLServer(complete)) {
            if (cbxSQLServerBanco.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um banco de dados!", "SQL Server",
                        JOptionPane.WARNING_MESSAGE);
                showPanel(tgbSQLServer, pnlSQLServer);
                cbxSQLServerBanco.requestFocus();
                return false;
            } else {
                try {
                    String Url = "jdbc:sqlserver://" + txtSQLServerHost.getText() + ":1433;DatabaseName="
                            + cbxSQLServerBanco.getSelectedItem().toString() + ";user="
                            + txtSQLServerUsuario.getText() + ";Password=" + txtSQLServerSenha.getText()
                            + ";CharacterSet=UTF-8";

                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    Properties prop = new Properties();
                    prop.put("charSet", "iso-8859-1");

                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    connSQLServer = DriverManager.getConnection(Url, prop);

                    this.setCursor(Cursor.getDefaultCursor());

                    if (showMessage) {
                        JOptionPane.showMessageDialog(this,
                                "Conexão estabelecida com sucesso! Info: "
                                        + connSQLServer.getMetaData().getDatabaseProductName(),
                                "SQL Server", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (HeadlessException | ClassNotFoundException | SQLException e) {
                    this.setCursor(Cursor.getDefaultCursor());

                    JOptionPane
                            .showMessageDialog(this,
                                    "<html><body><p style='width: 400px;'>Problemas ao se conectar!<br>"
                                            + e.getMessage() + "</p></body></html>",
                                    "SQL Server", JOptionPane.ERROR_MESSAGE);

                    return false;
                }
            }
            return true;
        } else
            return false;
    }

    private boolean valSyBase9Conexao(boolean showMessage) {
        if (valSyBase9()) {
            try {
                // String Url = "jdbc:odbc:" + txtSyBase9Host.getText();
                String Url = "jdbc:sybase:Tds:" + txtSyBase9Host.getText();

                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                Properties prop = new Properties();
                // prop.put("charSet", "ISO_1");
                prop.put("ServiceName", txtSyBase9Servico.getText());
                prop.put("user", txtSyBase9Usuario.getText());
                prop.put("password", txtSyBase9Senha.getText());

                // Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                Class.forName("com.sybase.jdbc3.jdbc.SybDriver");
                connSyBase9 = DriverManager.getConnection(Url, prop);

                this.setCursor(Cursor.getDefaultCursor());

                if (showMessage) {
                    JOptionPane.showMessageDialog(this,
                            "Conexão estabelecida com sucesso! Info: "
                                    + connSyBase9.getMetaData().getDatabaseProductName(),
                            "Conexão", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (HeadlessException | ClassNotFoundException | SQLException e) {
                this.setCursor(Cursor.getDefaultCursor());

                JOptionPane.showMessageDialog(this, "<html><body><p style='width: 400px;'>Problemas ao se conectar!<br>"
                        + e.getMessage() + "</p></body></html>", "Error", JOptionPane.ERROR_MESSAGE);

                return false;
            }
            return true;
        } else
            return false;
    }

    private void showPanel(JToggleButton tglbtn, JPanel pnl) {
        tgbInicio.setSelected(false);
        tgbSQLServer.setSelected(false);
        tgbSyBase9.setSelected(false);
        tgbTabelas.setSelected(false);
        tgbMigracao.setSelected(false);
        tglbtn.setSelected(true);

        pnlInicio.setVisible(false);
        pnlSQLServer.setVisible(false);
        pnlSyBase9.setVisible(false);
        pnlTabelas.setVisible(false);
        pnlMigracao.setVisible(false);
        pnl.setVisible(true);

        pnlOpened = pnl;

        switch (pnlOpened.getName()) {
            case "pnlInicio":
                btnVoltar.setEnabled(false);
                btnAvancar.setEnabled(true);
                btnVoltar.setText("Voltar");
                btnAvancar.setText("Avançar");
                break;
            case "pnlSQLServer":
            case "pnlSyBase9":
                btnVoltar.setEnabled(true);
                btnAvancar.setEnabled(true);
                btnVoltar.setText("Voltar");
                btnAvancar.setText("Avançar");
                break;
            case "pnlTabelas":
                btnVoltar.setEnabled(true);
                btnAvancar.setEnabled(true);
                btnVoltar.setText("Voltar");
                btnAvancar.setText("Migrar");
                break;
            case "pnlMigracao":
                btnVoltar.setEnabled(true);
                btnAvancar.setEnabled(false);
                btnVoltar.setText("Cancelar");
                btnAvancar.setText("Avançar");
                break;
        }
    }

    private void navTo(String act) {
        switch (act) {
            case "Avanc":
                switch (pnlOpened.getName()) {
                    case "pnlInicio":
                        switch (direcao.getId()) {
                            case 1:
                                showPanel(tgbSQLServer, pnlSQLServer);
                                break;
                            case 2:
                                showPanel(tgbSyBase9, pnlSyBase9);
                                break;
                        }
                        break;
                    case "pnlSQLServer":
                        if (valSQLServerConexao(false, true)) {
                            switch (direcao.getId()) {
                                case 1:
                                    showPanel(tgbSyBase9, pnlSyBase9);
                                    break;
                                case 2:
                                    showPanel(tgbTabelas, pnlTabelas);
                                    loadTables();
                                    break;
                            }
                        }
                        break;
                    case "pnlSyBase9":
                        if (valSyBase9Conexao(false)) {
                            switch (direcao.getId()) {
                                case 1:
                                    showPanel(tgbTabelas, pnlTabelas);
                                    loadTables();
                                    break;
                                case 2:
                                    showPanel(tgbSQLServer, pnlSQLServer);
                                    break;
                            }

                        }
                        break;
                    case "pnlTabelas":
                        if (valSQLServerConexao(false, true) && valSyBase9Conexao(false)) {
                            showPanel(tgbMigracao, pnlMigracao);
                            execMig();
                        }
                        break;
                }
                break;

            case "Volt":
                switch (pnlOpened.getName()) {
                    case "pnlSQLServer":
                        switch (direcao.getId()) {
                            case 1:
                                showPanel(tgbInicio, pnlInicio);
                                break;
                            case 2:
                                showPanel(tgbSyBase9, pnlSyBase9);
                                break;
                        }
                        break;
                    case "pnlSyBase9":
                        switch (direcao.getId()) {
                            case 1:
                                showPanel(tgbSQLServer, pnlSQLServer);
                                break;
                            case 2:
                                showPanel(tgbInicio, pnlInicio);
                                break;
                        }
                        break;
                    case "pnlTabelas":
                        switch (direcao.getId()) {
                            case 1:
                                showPanel(tgbSyBase9, pnlSyBase9);
                                break;
                            case 2:
                                showPanel(tgbSQLServer, pnlSQLServer);
                                break;
                        }
                        break;
                    case "pnlMigracao":
                        switch (btnVoltar.getText()) {
                            case "Cancelar":
                                swMig.cancel(true);
                                btnVoltar.setText("Voltar");
                                break;

                            default:
                                showPanel(tgbTabelas, pnlTabelas);
                                break;
                        }
                        break;
                }
                break;
        }
    }

    private void loadTables() {
        try {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            DatabaseMetaData md = null;
            switch (direcao.getId()) {
                case 1:
                    md = connSQLServer.getMetaData();
                    break;
                case 2:
                    md = connSyBase9.getMetaData();
                    break;
            }

            if (md != null) {
                ResultSet rsTab = md.getTables(null, null, "%", new String[]{"TABLE"});
                DefaultListModel<Tabela> tabsSQLServer = new DefaultListModel<>();
                DefaultListModel<Tabela> tabsSyBase9 = new DefaultListModel<>();
                while (rsTab.next()) {
                    switch (direcao.getId()) {
                        case 1:
                            if (!rsTab.getString("TABLE_NAME").toLowerCase().contains(txtFiltro.getText().toLowerCase()) && !rsTab.getString("TABLE_SCHEM").equals("sys")) {
                                Tabela tabela = new Tabela(rsTab.getString("TABLE_SCHEM"), rsTab.getString("TABLE_NAME"), txtPrefixo.getText());
                                ResultSet rsCol = md.getColumns(null, tabela.getSchema(), tabela.getNome(), null);
                                Coluna col;
                                while (rsCol.next()) {
                                    col = new Coluna(FontDad.SQLServer, rsCol);
                                    tabela.adicCol(col);
                                }
                                tabsSQLServer.addElement(tabela);
                            }
                            break;
                        case 2:
                            if (!rsTab.getString("TABLE_NAME").toLowerCase().trim().contains(txtFiltro.getText().toLowerCase()) && rsTab.getString("TABLE_SCHEM").equals("bethadba")) {
                                Tabela tabela = new Tabela(rsTab.getString("TABLE_SCHEM"), rsTab.getString("TABLE_NAME"), txtPrefixo.getText());
                                ResultSet rsCol = md.getColumns(null, tabela.getSchema(), tabela.getNome(), null);
                                Coluna col;
                                System.out.println(tabela);
                                if (tabela.getNome().equals("btls_cf")) {
                                    while (rsCol.next()) {
                                        col = new Coluna(FontDad.SyBase9, rsCol);
                                        System.out.println("  -> " + col);
                                        tabela.adicCol(col);
                                    }
                                }
                                tabsSyBase9.addElement(tabela);
                            }
                            break;
                    }
                }
                lstSQLServer.setModel(tabsSQLServer);
                lstSyBase9.setModel(tabsSyBase9);
            } else {
                JOptionPane.showMessageDialog(this,
                        "<html><body><p style='width: 400px;'>Problemas ao carregar tabelas!<br>Não foi possível acessar os meta dados.</p></body></html>",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            this.setCursor(Cursor.getDefaultCursor());
        } catch (NullPointerException | SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "<html><body><p style='width: 400px;'>Problemas ao carregar tabelas!<br>" + e.getMessage()
                            + "</p></body></html>",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    private void execMig() {
        swMig = new SwingWorker<String, Object>() {
            Tabela tabela = null;
            final ListModel<Tabela> tabsSyBase9 = lstSyBase9.getModel();
            final int tabsSyBase9Count = tabsSyBase9.getSize();

            @Override
            public String doInBackground() throws SQLException {
                txaMigracao.setText("");
                prbMigracaoTabelas.setValue(0);
                prbMigracaoTabelas.setMaximum(tabsSyBase9Count);
                for (int i = 0; i < tabsSyBase9Count && !isCancelled(); i++) {
                    tabela = tabsSyBase9.getElementAt(i);
                    Statement stmtSyBase9 = null;
                    ResultSet rs = null;
                    String sql = "";
                    StringBuilder sqlRow = new StringBuilder();

                    prbMigracaoTabelas.setValue(prbMigracaoTabelas.getValue() + 1);
                    prbMigracaoTabelas.setString(tabela.toString() + " (" + (prbMigracaoTabelas.getValue() * 100) / tabsSyBase9Count + "%)");

                    prbMigracaoLinhas.setValue(0);
                    prbMigracaoLinhas.setString("");

                    try (
                            Statement stmtSQLServer = connSQLServer.createStatement()
                    ) {
                        txaMigracao.append("Migrando tabela: " + tabela.toString() + " - " + Util.getCurrentDateTime() + "\n");
                        txaMigracao.setCaretPosition(txaMigracao.getDocument().getLength());

                        stmtSyBase9 = connSyBase9.createStatement();
                        sql = tabela.toSQLDrop().toString() + " " + tabela.toSQLCreate().toString();
                        stmtSyBase9.executeUpdate(sql);

                        sql = "SELECT COUNT(*) over (partition by 1) [rowTotal], * FROM " + tabela.getSchema() + "."
                                + tabela.getNome();
                        rs = stmtSQLServer.executeQuery(sql);

                        prbMigracaoLinhas.setValue(0);
                        prbMigracaoLinhas.setString("");

                        int row = 0, rowCount = 0, rowTotal;

                        while (rs.next() && !isCancelled()) {
                            rowTotal = rs.getInt("rowTotal");

                            prbMigracaoLinhas.setMaximum(rowTotal);

                            row++;
                            rowCount++;

                            prbMigracaoLinhas.setValue(rowCount);
                            prbMigracaoLinhas.setString((rowCount) + " de " + rowTotal + " (" + (rowCount * 100) / rowTotal + "%)");

                            sqlRow.append(tabela.toSQLInsert(rs).toString());

                            if (row == Integer.parseInt(txtLinhasBatch.getText()) || rowCount == rowTotal) {
                                stmtSyBase9.close();
                                stmtSyBase9 = connSyBase9.createStatement();
                                stmtSyBase9.executeUpdate(sqlRow.toString());

                                sqlRow = new StringBuilder();

                                row = 0;
                            }
                        }
                    } catch (SQLException e) {
                        if (!swMig.isCancelled()) {
                            txaMigracao.append("Erro: " + e.getMessage() + "\n" + "SQL: " + sql + "\n");
                            txaMigracao.setCaretPosition(txaMigracao.getDocument().getLength());
                        }
                    } finally {
                        if (rs != null) rs.close();
                        if (stmtSyBase9 != null) stmtSyBase9.close();
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    if (connSQLServer != null) connSQLServer.close();
                    if (connSyBase9 != null) connSyBase9.close();

                    if (swMig.isCancelled()) {
                        txaMigracao.append("Migração cancelada em " + " - " + Util.getCurrentDateTime() + "\n");
                    } else {
                        txaMigracao.append("Migração finalizada em " + " - " + Util.getCurrentDateTime() + "\n");
                    }

                    txaMigracao.setCaretPosition(txaMigracao.getDocument().getLength());

                    btnVoltar.setText("Voltar");
                } catch (Exception e) {
                    txaMigracao.append("Erro: " + e.getMessage() + "\n");
                    txaMigracao.setCaretPosition(txaMigracao.getDocument().getLength());
                }
            }
        };

        swMig.execute();
    }

    private void setCbxDirecaoActionPerformed(java.awt.event.ActionEvent evt) {
        direcao = (Direcao) cbxDirecao.getSelectedItem();
        if (direcao != null) {
            switch (direcao.getId()) {
                case 1:
                    tgbSQLServer.setBounds(10, 45, 125, 23);
                    tgbSyBase9.setBounds(10, 79, 125, 23);
                    break;
                case 2:
                    tgbSQLServer.setBounds(10, 79, 125, 23);
                    tgbSyBase9.setBounds(10, 45, 125, 23);
                    break;
            }
        }
    }

    private void btnSQLServerAtualizarBancoActionPerformed(java.awt.event.ActionEvent evt) {
        if (valSQLServer(false)) {
            ResultSet rs;
            try {
                String Url = "jdbc:sqlserver://" + txtSQLServerHost.getText() + ":1433;user="
                        + txtSQLServerUsuario.getText() + ";Password=" + txtSQLServerSenha.getText();

                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                Connection conn = DriverManager.getConnection(Url);

                DatabaseMetaData meta = conn.getMetaData();
                rs = meta.getCatalogs();
                cbxSQLServerBanco.removeAllItems();
                while (rs.next()) {
                    cbxSQLServerBanco.addItem(rs.getString("TABLE_CAT"));
                }
                rs.close();

                this.setCursor(Cursor.getDefaultCursor());
            } catch (ClassNotFoundException | SQLException e) {
                this.setCursor(Cursor.getDefaultCursor());

                JOptionPane.showMessageDialog(this, "<html><body><p style='width: 400px;'>Problemas ao se conectar!<br>"
                        + e.getMessage() + "</p></body></html>", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
