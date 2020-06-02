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
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.rvleustaquio.dbmigra.enums.FontDad;
import com.rvleustaquio.dbmigra.model.Coluna;
import com.rvleustaquio.dbmigra.model.Tabela;
import com.rvleustaquio.dbmigra.utils.Util;

public class App extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private Connection connFontDad;
    private Connection connSyBase9;
    private SwingWorker<String, Object> swMig;
    private JPanel pnlOpened;

    // region Components Declarations
    private JPanel pnlNav;
    private JPanel pnlFuncs;
    private JPanel pnlInicio;
    private JPanel pnlFonteDados;
    private JPanel pnlSyBase9;
    private JPanel pnlTabelas;
    private JPanel pnlMigracao;

    private JToggleButton tgbInicio;
    private JToggleButton tgbFonteDados;
    private JToggleButton tgbSyBase9;
    private JToggleButton tgbTabelas;
    private JToggleButton tgbMigracao;

    private JButton btnSair;
    private JButton btnAvancar;
    private JButton btnVoltar;
    private JButton btnFonteDadosAtualizarBanco;
    private JButton btnFonteDadosTestarConexao;
    private JButton btnSyBase9TestarConexao;
    private JButton btnFonteDadosAdicionaTodos;
    private JButton btnFonteDadosAdicionaUm;
    private JButton btnSyBase9RemoveTodos;
    private JButton btnSyBase9RemoveUm;

    private JLabel lblInicio;
    private JLabel lblConfiguracoes;
    private JLabel lblPrefixo;
    private JLabel lblFiltro;
    private JLabel lblLinhasBatch;
    private JLabel lblFonteDados;
    private JLabel lblFonteDadosHost;
    private JLabel lblFonteDadosUsuario;
    private JLabel lblFonteDadosSenha;
    private JLabel lblFonteDadosBanco;
    private JLabel lblSyBase9;
    private JLabel lblSyBase9Host;
    private JLabel lblSyBase9Servico;
    private JLabel lblSyBase9Usuario;
    private JLabel lblSyBase9Senha;
    private JLabel lblFonteDadosTabelas;
    private JLabel lblSyBase9Tabelas;
    private JLabel lblMigracao;
    private JLabel lblQntTabelas;
    private JLabel lblQntLinhas;

    private JTextField txtPrefixo;
    private JTextField txtFiltro;
    private JTextField txtLinhasBatch;
    private JTextField txtFonteDadosHost;
    private JTextField txtFonteDadosUsuario;
    private JTextField txtFonteDadosSenha;
    private JTextField txtSyBase9Host;
    private JTextField txtSyBase9Servico;
    private JTextField txtSyBase9Usuario;
    private JTextField txtSyBase9Senha;

    private JComboBox<String> cbxFonteDadosBanco;

    private JSeparator sepConfiguracoes;
    private JSeparator sepFonteDados;
    private JSeparator sepSyBase9;

    private JScrollPane spFonteDados;
    private JScrollPane spSyBase9;
    private JScrollPane spMigracao;

    private JList<Tabela> lstFonteDados;
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

    private void initialize() {
        setTitle("DBMIGRA");
        setResizable(false);
        setBounds(100, 100, 780, 490);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        tgbFonteDados = new JToggleButton("Fonte de dados");
        tgbFonteDados.addActionListener(e -> showPanel((JToggleButton) e.getSource(), pnlFonteDados));
        tgbFonteDados.setBounds(10, 45, 125, 23);
        pnlNav.add(tgbFonteDados);

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

        pnlFonteDados = new JPanel();
        pnlFonteDados.setBounds(144, 0, 630, 413);
        pnlFonteDados.setLayout(null);
        pnlFonteDados.setName("pnlFonteDados");
        getContentPane().add(pnlFonteDados);

        lblFonteDados = new JLabel("Fonte de Dados");
        lblFonteDados.setBounds(10, 11, 113, 14);
        pnlFonteDados.add(lblFonteDados);

        lblFonteDadosHost = new JLabel("Host:");
        lblFonteDadosHost.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFonteDadosHost.setBounds(155, 135, 132, 14);
        pnlFonteDados.add(lblFonteDadosHost);

        txtFonteDadosHost = new JTextField("127.0.0.1\\SQLEXPRESS");
        txtFonteDadosHost.setBounds(297, 132, 200, 20);
        txtFonteDadosHost.setColumns(10);
        pnlFonteDados.add(txtFonteDadosHost);

        lblFonteDadosUsuario = new JLabel("Usuário:");
        lblFonteDadosUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFonteDadosUsuario.setBounds(155, 163, 132, 14);
        pnlFonteDados.add(lblFonteDadosUsuario);

        txtFonteDadosUsuario = new JTextField("sa");
        txtFonteDadosUsuario.setColumns(10);
        txtFonteDadosUsuario.setBounds(297, 160, 200, 20);
        pnlFonteDados.add(txtFonteDadosUsuario);

        lblFonteDadosSenha = new JLabel("Senha:");
        lblFonteDadosSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFonteDadosSenha.setBounds(155, 191, 132, 14);
        pnlFonteDados.add(lblFonteDadosSenha);

        txtFonteDadosSenha = new JTextField("b5th4@custom");
        txtFonteDadosSenha.setColumns(10);
        txtFonteDadosSenha.setBounds(297, 188, 200, 20);
        pnlFonteDados.add(txtFonteDadosSenha);

        lblFonteDadosBanco = new JLabel("Banco de Dados:");
        lblFonteDadosBanco.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFonteDadosBanco.setBounds(155, 219, 132, 14);
        pnlFonteDados.add(lblFonteDadosBanco);

        cbxFonteDadosBanco = new JComboBox<>();
        cbxFonteDadosBanco.setBounds(297, 217, 200, 20);
        pnlFonteDados.add(cbxFonteDadosBanco);

        btnFonteDadosAtualizarBanco = new JButton("...");
        btnFonteDadosAtualizarBanco.addActionListener(this::btnFonteDadosAtualizarBancoActionPerformed);
        btnFonteDadosAtualizarBanco.setBounds(507, 217, 22, 20);
        pnlFonteDados.add(btnFonteDadosAtualizarBanco);

        sepFonteDados = new JSeparator();
        sepFonteDados.setBounds(155, 248, 374, 2);
        pnlFonteDados.add(sepFonteDados);

        btnFonteDadosTestarConexao = new JButton("Testar Conexão");
        btnFonteDadosTestarConexao.addActionListener(e -> valFonteDadosConexao(true, false));
        btnFonteDadosTestarConexao.setBounds(397, 261, 132, 23);
        pnlFonteDados.add(btnFonteDadosTestarConexao);

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

        lblFonteDadosTabelas = new JLabel("Tabelas disponíveis");
        lblFonteDadosTabelas.setBounds(10, 11, 173, 14);
        pnlTabelas.add(lblFonteDadosTabelas);

        spFonteDados = new JScrollPane();
        spFonteDados.setBounds(10, 36, 300, 328);
        pnlTabelas.add(spFonteDados);

        lstFonteDados = new JList<>();
        spFonteDados.setViewportView(lstFonteDados);

        lblSyBase9Tabelas = new JLabel("Tabelas a serem migradas");
        lblSyBase9Tabelas.setBounds(320, 11, 173, 14);
        pnlTabelas.add(lblSyBase9Tabelas);

        spSyBase9 = new JScrollPane();
        spSyBase9.setBounds(320, 36, 300, 328);
        pnlTabelas.add(spSyBase9);

        lstSyBase9 = new JList<>();
        spSyBase9.setViewportView(lstSyBase9);

        btnFonteDadosAdicionaUm = new JButton(">");
        btnFonteDadosAdicionaUm.addActionListener(e -> {
            DefaultListModel<Tabela> tabsSyBase9 = (DefaultListModel<Tabela>) lstSyBase9.getModel();
            for (Tabela tabela : lstFonteDados.getSelectedValuesList()) {
                if (!tabsSyBase9.contains(tabela)) {
                    tabsSyBase9.addElement(tabela);
                }
            }
            lstSyBase9.setModel(tabsSyBase9);
        });
        btnFonteDadosAdicionaUm.setBounds(199, 375, 50, 23);
        pnlTabelas.add(btnFonteDadosAdicionaUm);

        btnFonteDadosAdicionaTodos = new JButton(">>");
        btnFonteDadosAdicionaTodos.addActionListener(e -> {
            DefaultListModel<Tabela> tabsSyBase9 = (DefaultListModel<Tabela>) lstSyBase9.getModel();
            ListModel<Tabela> tabsFontDad = lstFonteDados.getModel();
            for (int i = 0; i < tabsFontDad.getSize(); i++) {
                tabsSyBase9.addElement(tabsFontDad.getElementAt(i));
            }
            lstSyBase9.setModel(tabsSyBase9);
        });
        btnFonteDadosAdicionaTodos.setBounds(259, 375, 50, 23);
        pnlTabelas.add(btnFonteDadosAdicionaTodos);

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

    private boolean valFonteDados(boolean complete) {
        if (txtFonteDadosHost.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o host!", "Fonte de Dados",
                    JOptionPane.WARNING_MESSAGE);
            txtFonteDadosHost.requestFocus();
            return false;
        }
        if (txtFonteDadosUsuario.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o usuário!", "Fonte de Dados",
                    JOptionPane.WARNING_MESSAGE);
            txtFonteDadosUsuario.requestFocus();
            return false;
        }
        if (txtFonteDadosSenha.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o senha!", "Fonte de Dados",
                    JOptionPane.WARNING_MESSAGE);
            txtFonteDadosSenha.requestFocus();
            return false;
        }
        if (complete) {
            if (cbxFonteDadosBanco.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um banco de dados!", "Fonte de Dados",
                        JOptionPane.WARNING_MESSAGE);
                cbxFonteDadosBanco.requestFocus();
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

    private boolean valFonteDadosConexao(boolean showMessage, boolean complete) {
        if (valFonteDados(complete)) {
            if (cbxFonteDadosBanco.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um banco de dados!", "Fonte de Dados",
                        JOptionPane.WARNING_MESSAGE);
                showPanel(tgbFonteDados, pnlFonteDados);
                cbxFonteDadosBanco.requestFocus();
                return false;
            } else {
                try {
                    String Url = "jdbc:sqlserver://" + txtFonteDadosHost.getText() + ":1433;DatabaseName="
                            + cbxFonteDadosBanco.getSelectedItem().toString() + ";user="
                            + txtFonteDadosUsuario.getText() + ";Password=" + txtFonteDadosSenha.getText()
                            + ";CharacterSet=UTF-8";

                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    Properties prop = new Properties();
                    prop.put("charSet", "iso-8859-1");

                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    connFontDad = DriverManager.getConnection(Url, prop);

                    this.setCursor(Cursor.getDefaultCursor());

                    if (showMessage) {
                        JOptionPane.showMessageDialog(this,
                                "Conexão estabelecida com sucesso! Info: "
                                        + connFontDad.getMetaData().getDatabaseProductName(),
                                "Fonte de Dados", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (HeadlessException | ClassNotFoundException | SQLException e) {
                    this.setCursor(Cursor.getDefaultCursor());

                    JOptionPane
                            .showMessageDialog(this,
                                    "<html><body><p style='width: 400px;'>Problemas ao se conectar!<br>"
                                            + e.getMessage() + "</p></body></html>",
                                    "Fonte de Dados", JOptionPane.ERROR_MESSAGE);

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
        tgbFonteDados.setSelected(false);
        tgbSyBase9.setSelected(false);
        tgbTabelas.setSelected(false);
        tgbMigracao.setSelected(false);
        tglbtn.setSelected(true);

        pnlInicio.setVisible(false);
        pnlFonteDados.setVisible(false);
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
            case "pnlFonteDados":
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
                        showPanel(tgbFonteDados, pnlFonteDados);
                        break;
                    case "pnlFonteDados":
                        if (valFonteDadosConexao(false, true)) {
                            showPanel(tgbSyBase9, pnlSyBase9);
                        }
                        break;
                    case "pnlSyBase9":
                        if (valSyBase9Conexao(false)) {
                            showPanel(tgbTabelas, pnlTabelas);
                            loadTables();
                        }
                        break;
                    case "pnlTabelas":
                        if (valFonteDadosConexao(false, true) && valSyBase9Conexao(false)) {
                            showPanel(tgbMigracao, pnlMigracao);
                            execMig();
                        }
                        break;
                }
                break;

            case "Volt":
                switch (pnlOpened.getName()) {
                    case "pnlFonteDados":
                        showPanel(tgbInicio, pnlInicio);
                        break;
                    case "pnlSyBase9":
                        showPanel(tgbFonteDados, pnlFonteDados);
                        break;
                    case "pnlTabelas":
                        showPanel(tgbSyBase9, pnlSyBase9);
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

            DatabaseMetaData md = connFontDad.getMetaData();
            ResultSet rsTab = md.getTables(null, null, "%", new String[]{"TABLE"});
            DefaultListModel<Tabela> tabsFontDad = new DefaultListModel<>();
            DefaultListModel<Tabela> tabsSyBase9 = new DefaultListModel<>();
            while (rsTab.next()) {
                if (!rsTab.getString("TABLE_NAME").toLowerCase().contains(txtFiltro.getText().toLowerCase()) && !rsTab.getString("TABLE_SCHEM").equals("sys")) {
                    Tabela tabela = new Tabela(rsTab.getString("TABLE_SCHEM"), rsTab.getString("TABLE_NAME"), txtPrefixo.getText());
                    ResultSet rsCol = md.getColumns(null, tabela.getSchema(), tabela.getNome(), null);
                    Coluna col;
                    while (rsCol.next()) {
                        col = new Coluna(FontDad.SQLServer, rsCol);
                        tabela.adicCol(col);
                    }
                    tabsFontDad.addElement(tabela);
                }
            }
            lstFonteDados.setModel(tabsFontDad);
            lstSyBase9.setModel(tabsSyBase9);

            this.setCursor(Cursor.getDefaultCursor());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "<html><body><p style='width: 400px;'>Problemas ao carregar tabelas!<br>" + e.getMessage()
                            + "</p></body></html>",
                    "Error", JOptionPane.ERROR_MESSAGE);
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
                            Statement stmtFontDad = connFontDad.createStatement()
                    ) {
                        txaMigracao.append("Migrando tabela: " + tabela.toString() + " - " + Util.getCurrentDateTime() + "\n");
                        txaMigracao.setCaretPosition(txaMigracao.getDocument().getLength());

                        stmtSyBase9 = connSyBase9.createStatement();
                        sql = tabela.toSQLDrop().toString() + " " + tabela.toSQLCreate().toString();
                        stmtSyBase9.executeUpdate(sql);

                        sql = "SELECT COUNT(*) over (partition by 1) [rowTotal], * FROM " + tabela.getSchema() + "."
                                + tabela.getNome();
                        rs = stmtFontDad.executeQuery(sql);

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
                    if (connFontDad != null) connFontDad.close();
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

    private void btnFonteDadosAtualizarBancoActionPerformed(java.awt.event.ActionEvent evt) {
        if (valFonteDados(false)) {
            ResultSet rs;
            try {
                String Url = "jdbc:sqlserver://" + txtFonteDadosHost.getText() + ":1433;user="
                        + txtFonteDadosUsuario.getText() + ";Password=" + txtFonteDadosSenha.getText();

                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                Connection conn = DriverManager.getConnection(Url);

                DatabaseMetaData meta = conn.getMetaData();
                rs = meta.getCatalogs();
                cbxFonteDadosBanco.removeAllItems();
                while (rs.next()) {
                    cbxFonteDadosBanco.addItem(rs.getString("TABLE_CAT"));
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
