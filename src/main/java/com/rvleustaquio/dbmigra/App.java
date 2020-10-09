package com.rvleustaquio.dbmigra;

import com.rvleustaquio.dbmigra.enums.FontDad;
import com.rvleustaquio.dbmigra.model.Coluna;
import com.rvleustaquio.dbmigra.model.Tabela;
import com.rvleustaquio.dbmigra.utils.Configs;
import com.rvleustaquio.dbmigra.utils.Util;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class App extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private Connection connSQLServer;
    private Connection connSyBase9;
    private SwingWorker<String, Object> swMig;
    private JPanel pnlOpened;
    private FontDad direcao;
    private final Configs confs = new Configs();

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
    private JButton btnDeAdicionaTodas;
    private JButton btnDeAdicionaUma;
    private JButton btnParaRemoveTodas;
    private JButton btnParaRemoveUma;

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
    private JLabel lblSyBase9Schema;
    private JLabel lblDeTabelas;
    private JLabel lblParaTabelas;
    private JLabel lblMigracao;
    private JLabel lblQntTabelas;
    private JLabel lblQntLinhas;

    private JTextField txtPrefixo;
    private JTextField txtFiltro;
    private JTextField txtLinhasBatch;
    private JTextField txtSQLServerHost;
    private JTextField txtSQLServerUsuario;
    private JPasswordField txtSQLServerSenha;
    private JTextField txtSyBase9Host;
    private JTextField txtSyBase9Servico;
    private JTextField txtSyBase9Usuario;
    private JPasswordField txtSyBase9Senha;
    private JTextField txtSyBase9Schema;

    private JComboBox<String> cbxSQLServerBanco;
    private JComboBox<FontDad> cbxDirecao;

    private JSeparator sepConfiguracoes;
    private JSeparator sepSQLServer;
    private JSeparator sepSyBase9;

    private JScrollPane spDe;
    private JScrollPane spPara;
    private JScrollPane spMigracao;

    private JList<Tabela> lstTabsDe;
    private JList<Tabela> lstTabsPara;

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
        setBounds(100, 100, 790, 500);
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

        //region Painel Inicial
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

        txtPrefixo = new JTextField(confs.getPropValues("ini.prefixo"));
        txtPrefixo.setBounds(317, 214, 127, 20);
        txtPrefixo.setColumns(10);
        pnlInicio.add(txtPrefixo);

        lblFiltro = new JLabel("Filtro:");
        lblFiltro.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFiltro.setBounds(181, 245, 126, 14);
        pnlInicio.add(lblFiltro);

        txtFiltro = new JTextField(confs.getPropValues("ini.filtro"));
        txtFiltro.setBounds(317, 242, 127, 20);
        txtFiltro.setColumns(10);
        pnlInicio.add(txtFiltro);

        lblLinhasBatch = new JLabel("Regs/Pacote:");
        lblLinhasBatch.setHorizontalAlignment(SwingConstants.RIGHT);
        lblLinhasBatch.setBounds(181, 273, 126, 14);
        pnlInicio.add(lblLinhasBatch);

        txtLinhasBatch = new JTextField(confs.getPropValues("ini.regs-pacote"));
        txtLinhasBatch.setBounds(317, 270, 127, 20);
        txtLinhasBatch.setColumns(10);
        pnlInicio.add(txtLinhasBatch);

        lblDirecao = new JLabel("Fonte de dados:");
        lblDirecao.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDirecao.setBounds(181, 301, 126, 14);
        pnlInicio.add(lblDirecao);

        direcao = FontDad.fromId(Integer.parseInt(confs.getPropValues("ini.direcao")));

        cbxDirecao = new JComboBox<>();
        cbxDirecao.setModel(new DefaultComboBoxModel<>(FontDad.values()));
        cbxDirecao.setSelectedItem(direcao);
        cbxDirecao.addActionListener(e -> changeDirecao());
        cbxDirecao.setBounds(317, 298, 170, 20);
        pnlInicio.add(cbxDirecao);

        changeDirecao();
        //endregion

        //region Painel do SQL Server
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

        txtSQLServerHost = new JTextField(confs.getPropValues("sqlsrv.host"));
        txtSQLServerHost.setBounds(297, 132, 200, 20);
        txtSQLServerHost.setColumns(10);
        pnlSQLServer.add(txtSQLServerHost);

        lblSQLServerUsuario = new JLabel("Usuário:");
        lblSQLServerUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSQLServerUsuario.setBounds(155, 163, 132, 14);
        pnlSQLServer.add(lblSQLServerUsuario);

        txtSQLServerUsuario = new JTextField(confs.getPropValues("sqlsrv.usuario"));
        txtSQLServerUsuario.setColumns(10);
        txtSQLServerUsuario.setBounds(297, 160, 200, 20);
        pnlSQLServer.add(txtSQLServerUsuario);

        lblSQLServerSenha = new JLabel("Senha:");
        lblSQLServerSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSQLServerSenha.setBounds(155, 191, 132, 14);
        pnlSQLServer.add(lblSQLServerSenha);

        txtSQLServerSenha = new JPasswordField(confs.getPropValues("sqlsrv.senha"));
        txtSQLServerSenha.setColumns(10);
        txtSQLServerSenha.setBounds(297, 188, 200, 20);
        pnlSQLServer.add(txtSQLServerSenha);

        lblSQLServerBanco = new JLabel("Banco de Dados:");
        lblSQLServerBanco.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSQLServerBanco.setBounds(155, 219, 132, 14);
        pnlSQLServer.add(lblSQLServerBanco);

        cbxSQLServerBanco = new JComboBox<>();
        cbxSQLServerBanco.setBounds(297, 217, 200, 20);
        cbxSQLServerBanco.setSelectedItem(confs.getPropValues("sqlsrv.db"));
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
        //endregion

        //region Painel do SyBase 9
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
        lblSyBase9Host.setBounds(155, 125, 132, 14);
        pnlSyBase9.add(lblSyBase9Host);

        txtSyBase9Host = new JTextField(confs.getPropValues("syb9.host"));
        txtSyBase9Host.setBounds(297, 122, 200, 20);
        txtSyBase9Host.setColumns(10);
        pnlSyBase9.add(txtSyBase9Host);

        lblSyBase9Servico = new JLabel("Banco de Dados:");
        lblSyBase9Servico.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSyBase9Servico.setBounds(155, 153, 132, 14);
        pnlSyBase9.add(lblSyBase9Servico);

        txtSyBase9Servico = new JTextField(confs.getPropValues("syb9.servico"));
        txtSyBase9Servico.setColumns(10);
        txtSyBase9Servico.setBounds(297, 150, 200, 20);
        pnlSyBase9.add(txtSyBase9Servico);

        lblSyBase9Usuario = new JLabel("Usuário:");
        lblSyBase9Usuario.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSyBase9Usuario.setBounds(155, 182, 132, 14);
        pnlSyBase9.add(lblSyBase9Usuario);

        txtSyBase9Usuario = new JTextField(confs.getPropValues("syb9.usuario"));
        txtSyBase9Usuario.setColumns(10);
        txtSyBase9Usuario.setBounds(297, 179, 200, 20);
        pnlSyBase9.add(txtSyBase9Usuario);

        lblSyBase9Senha = new JLabel("Senha:");
        lblSyBase9Senha.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSyBase9Senha.setBounds(155, 210, 132, 14);
        pnlSyBase9.add(lblSyBase9Senha);

        txtSyBase9Senha = new JPasswordField(confs.getPropValues("syb9.senha"));
        txtSyBase9Senha.setColumns(10);
        txtSyBase9Senha.setBounds(297, 207, 200, 20);
        pnlSyBase9.add(txtSyBase9Senha);

        lblSyBase9Schema = new JLabel("Filtrar Schema:");
        lblSyBase9Schema.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSyBase9Schema.setBounds(155, 238, 132, 14);
        pnlSyBase9.add(lblSyBase9Schema);

        txtSyBase9Schema = new JTextField(confs.getPropValues("syb9.schema"));
        txtSyBase9Schema.setColumns(10);
        txtSyBase9Schema.setBounds(297, 235, 200, 20);
        pnlSyBase9.add(txtSyBase9Schema);

        sepSyBase9 = new JSeparator();
        sepSyBase9.setBounds(155, 265, 374, 2);
        pnlSyBase9.add(sepSyBase9);

        btnSyBase9TestarConexao = new JButton("Testar Conexão");
        btnSyBase9TestarConexao.setBounds(397, 278, 132, 23);
        btnSyBase9TestarConexao.addActionListener(e -> valSyBase9Conexao(true));
        pnlSyBase9.add(btnSyBase9TestarConexao);
        //endregion

        //region Painel das Tabelas
        pnlTabelas = new JPanel();
        pnlTabelas.setBounds(144, 0, 630, 413);
        pnlTabelas.setLayout(null);
        pnlTabelas.setName("pnlTabelas");
        getContentPane().add(pnlTabelas);

        lblDeTabelas = new JLabel("Tabelas disponíveis");
        lblDeTabelas.setBounds(10, 11, 173, 14);
        pnlTabelas.add(lblDeTabelas);

        spDe = new JScrollPane();
        spDe.setBounds(10, 36, 300, 328);
        pnlTabelas.add(spDe);

        lstTabsDe = new JList<>();
        spDe.setViewportView(lstTabsDe);

        lblParaTabelas = new JLabel("Tabelas a serem migradas");
        lblParaTabelas.setBounds(320, 11, 173, 14);
        pnlTabelas.add(lblParaTabelas);

        spPara = new JScrollPane();
        spPara.setBounds(320, 36, 300, 328);
        pnlTabelas.add(spPara);

        lstTabsPara = new JList<>();
        spPara.setViewportView(lstTabsPara);

        btnDeAdicionaUma = new JButton(">");
        btnDeAdicionaUma.addActionListener(e -> {
            DefaultListModel<Tabela> tabsPara = (DefaultListModel<Tabela>) lstTabsPara.getModel();
            for (Tabela tabela : lstTabsDe.getSelectedValuesList()) {
                if (!tabsPara.contains(tabela)) {
                    tabsPara.addElement(tabela);
                }
            }
            lstTabsPara.setModel(tabsPara);
        });
        btnDeAdicionaUma.setBounds(199, 375, 50, 23);
        pnlTabelas.add(btnDeAdicionaUma);

        btnDeAdicionaTodas = new JButton(">>");
        btnDeAdicionaTodas.addActionListener(e -> {
            DefaultListModel<Tabela> tabsPara = (DefaultListModel<Tabela>) lstTabsPara.getModel();
            ListModel<Tabela> tabsSQLServer = lstTabsDe.getModel();
            for (int i = 0; i < tabsSQLServer.getSize(); i++) {
                tabsPara.addElement(tabsSQLServer.getElementAt(i));
            }
            lstTabsPara.setModel(tabsPara);
        });
        btnDeAdicionaTodas.setBounds(259, 375, 50, 23);
        pnlTabelas.add(btnDeAdicionaTodas);

        btnParaRemoveTodas = new JButton("<<");
        btnParaRemoveTodas.addActionListener(e -> {
            DefaultListModel<Tabela> tabsPara = new DefaultListModel<>();
            lstTabsPara.setModel(tabsPara);
        });
        btnParaRemoveTodas.setBounds(320, 375, 50, 23);
        pnlTabelas.add(btnParaRemoveTodas);

        btnParaRemoveUma = new JButton("<");
        btnParaRemoveUma.addActionListener(e -> {
            DefaultListModel<Tabela> tabsPara = (DefaultListModel<Tabela>) lstTabsPara.getModel();
            List<Tabela> tabsParaSel = lstTabsPara.getSelectedValuesList();
            for (Tabela tabela : tabsParaSel) {
                tabsPara.removeElement(tabela);
            }
        });
        btnParaRemoveUma.setBounds(380, 375, 50, 23);
        pnlTabelas.add(btnParaRemoveUma);
        //endregion

        //region Painel da Migração
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
        //endregion
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
        if ((new String(txtSQLServerSenha.getPassword())).equals("")) {
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
        if ((new String(txtSyBase9Senha.getPassword())).equals("")) {
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
                            + txtSQLServerUsuario.getText() + ";Password=" + (new String(txtSQLServerSenha.getPassword()))
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
                prop.put("password", new String(txtSyBase9Senha.getPassword()));

                // Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                Class.forName("com.sybase.jdbc4.jdbc.SybDriver");
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
                        confs.open();
                        confs.setPropValues("ini.prefixo", txtPrefixo.getText());
                        confs.setPropValues("ini.filtro", txtFiltro.getText());
                        confs.setPropValues("ini.regs-pacote", txtLinhasBatch.getText());
                        confs.setPropValues("ini.direcao", String.valueOf(direcao.getId()));
                        confs.save();

                        switch (direcao) {
                            case SQLServer:
                                showPanel(tgbSQLServer, pnlSQLServer);
                                break;
                            case SyBase9:
                                showPanel(tgbSyBase9, pnlSyBase9);
                                break;
                        }
                        break;
                    case "pnlSQLServer":
                        confs.open();
                        confs.setPropValues("sqlsrv.host", txtSQLServerHost.getText());
                        confs.setPropValues("sqlsrv.usuario", txtSQLServerUsuario.getText());
                        confs.setPropValues("sqlsrv.senha", new String(txtSQLServerSenha.getPassword()));
                        confs.setPropValues("sqlsrv.db", Objects.requireNonNull(cbxSQLServerBanco.getSelectedItem()).toString());
                        confs.save();

                        if (valSQLServerConexao(false, true)) {
                            switch (direcao) {
                                case SQLServer:
                                    showPanel(tgbSyBase9, pnlSyBase9);
                                    break;
                                case SyBase9:
                                    showPanel(tgbTabelas, pnlTabelas);
                                    loadTables();
                                    break;
                            }
                        }
                        break;
                    case "pnlSyBase9":
                        confs.open();
                        confs.setPropValues("syb9.host", txtSyBase9Host.getText());
                        confs.setPropValues("syb9.usuario", txtSyBase9Usuario.getText());
                        confs.setPropValues("syb9.senha", new String(txtSyBase9Senha.getPassword()));
                        confs.setPropValues("syb9.servico", txtSyBase9Servico.getText());
                        confs.setPropValues("syb9.schema", txtSyBase9Schema.getText());
                        confs.save();

                        if (valSyBase9Conexao(false)) {
                            switch (direcao) {
                                case SQLServer:
                                    showPanel(tgbTabelas, pnlTabelas);
                                    loadTables();
                                    break;
                                case SyBase9:
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
                        switch (direcao) {
                            case SQLServer:
                                showPanel(tgbInicio, pnlInicio);
                                break;
                            case SyBase9:
                                showPanel(tgbSyBase9, pnlSyBase9);
                                break;
                        }
                        break;
                    case "pnlSyBase9":
                        switch (direcao) {
                            case SQLServer:
                                showPanel(tgbSQLServer, pnlSQLServer);
                                break;
                            case SyBase9:
                                showPanel(tgbInicio, pnlInicio);
                                break;
                        }
                        break;
                    case "pnlTabelas":
                        switch (direcao) {
                            case SQLServer:
                                showPanel(tgbSyBase9, pnlSyBase9);
                                break;
                            case SyBase9:
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
            ResultSet rsTab = null;
            switch (direcao) {
                case SQLServer:
                    md = connSQLServer.getMetaData();
                    rsTab = md.getTables(null, null, "%", new String[]{"TABLE"});
                    break;
                case SyBase9:
                    md = connSyBase9.getMetaData();
                    rsTab = md.getTables(null, txtSyBase9Schema.getText(), "%", new String[]{"TABLE"});
                    break;
            }

            if (rsTab != null) {
                DefaultListModel<Tabela> tabsSQLServer = new DefaultListModel<>();
                DefaultListModel<Tabela> tabsSyBase9 = new DefaultListModel<>();
                while (rsTab.next()) {
                    switch (direcao) {
                        case SQLServer:
                            if (!rsTab.getString("TABLE_NAME").toLowerCase().trim().contains(txtFiltro.getText().toLowerCase()) && !rsTab.getString("TABLE_SCHEM").equals("sys")) {
                                Tabela tabela = new Tabela(direcao, rsTab.getString("TABLE_SCHEM"), rsTab.getString("TABLE_NAME"), txtPrefixo.getText());
                                ResultSet rsCol = md.getColumns(null, tabela.getSchema(), tabela.getNome(), null);
                                Coluna col;
                                while (rsCol.next()) {
                                    col = new Coluna(FontDad.SQLServer, rsCol);
                                    tabela.adicCol(col);
                                }
                                tabsSQLServer.addElement(tabela);
                            }
                            break;
                        case SyBase9:
                            if (!rsTab.getString("TABLE_NAME").toLowerCase().trim().contains(txtFiltro.getText().toLowerCase()) &&
                                    !rsTab.getString("TABLE_NAME").toLowerCase().trim().contains("btls") &&
                                    !rsTab.getString("TABLE_NAME").toLowerCase().trim().contains("audit") &&
                                    !rsTab.getString("TABLE_NAME").toLowerCase().trim().contains("config") &&
                                    !rsTab.getString("TABLE_NAME").toLowerCase().trim().contains("cloud") &&
                                    !rsTab.getString("TABLE_NAME").toLowerCase().trim().contains("pbcat") &&
                                    !rsTab.getString("TABLE_NAME").toLowerCase().trim().contains("temp") &&
                                    (rsTab.getString("TABLE_SCHEM").equals("bethadba") || rsTab.getString("TABLE_SCHEM").equals("sapo"))) {
                                Tabela tabela = new Tabela(direcao, rsTab.getString("TABLE_SCHEM"), rsTab.getString("TABLE_NAME"), txtPrefixo.getText());
                                ResultSet rsCol = md.getColumns(null, tabela.getSchema(), tabela.getNome(), null);
                                Coluna col;
                                while (rsCol.next()) {
                                    col = new Coluna(FontDad.SyBase9, rsCol);
                                    tabela.adicCol(col);
                                }
                                tabsSyBase9.addElement(tabela);
                            }
                            break;
                    }
                }
                switch (direcao) {
                    case SQLServer:
                        lstTabsDe.setModel(tabsSQLServer);
                        lstTabsPara.setModel(tabsSyBase9);
                        break;
                    case SyBase9:
                        lstTabsDe.setModel(tabsSyBase9);
                        lstTabsPara.setModel(tabsSQLServer);
                        break;
                }
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
            final ListModel<Tabela> tabsPara = lstTabsPara.getModel();
            final int tabsParaCount = tabsPara.getSize();

            @Override
            public String doInBackground() throws SQLException {
                txaMigracao.setText("");
                prbMigracaoTabelas.setValue(0);
                prbMigracaoTabelas.setMaximum(tabsParaCount);
                for (int i = 0; i < tabsParaCount && !isCancelled(); i++) {
                    tabela = tabsPara.getElementAt(i);
                    Statement stmtDe = null;
                    Statement stmtPara = null;
                    ResultSet rs = null;
                    String sql = "";
                    StringBuilder sqlRow = new StringBuilder();

                    prbMigracaoTabelas.setValue(prbMigracaoTabelas.getValue() + 1);
                    prbMigracaoTabelas.setString(tabela.toString() + " (" + (prbMigracaoTabelas.getValue() * 100) / tabsParaCount + "%)");

                    prbMigracaoLinhas.setValue(0);
                    prbMigracaoLinhas.setString("");

                    try {
                        switch (direcao) {
                            case SQLServer:
                                stmtDe = connSQLServer.createStatement();
                                stmtPara = connSyBase9.createStatement();
                                break;
                            case SyBase9:
                                stmtDe = connSyBase9.createStatement();
                                stmtPara = connSQLServer.createStatement();
                                break;
                        }

                        txaMigracao.append("Migrando tabela: " + tabela.toString() + " - " + Util.getCurrentDateTime() + "\n");
                        txaMigracao.setCaretPosition(txaMigracao.getDocument().getLength());

                        sql = tabela.toSQLDrop().toString() + " " + tabela.toSQLCreate().toString();
                        stmtPara.executeUpdate(sql);

                        switch (direcao) {
                            case SQLServer:
                                sql = "SELECT COUNT(*) over (partition by 1) [rowTotal], * FROM " + tabela.getSchema() + "." + tabela.getNome();
                                break;
                            case SyBase9:
                                sql = "SELECT t.rowTotal, * FROM " + tabela.getSchema() + "." + tabela.getNome() + " cross join (select count() as rowTotal from " + tabela.getSchema() + "." + tabela.getNome() + ") as t";
                                break;
                        }

                        if (stmtDe != null)
                            rs = stmtDe.executeQuery(sql);
                        else {
                            swMig.cancel(true);
                            txaMigracao.append("Erro: Problemas ao preparar o procedimento!\n");
                            txaMigracao.setCaretPosition(txaMigracao.getDocument().getLength());
                        }

                        prbMigracaoLinhas.setValue(0);
                        prbMigracaoLinhas.setString("");

                        int row = 0, rowCount = 0, rowTotal;

                        if (rs != null) {
                            while (rs.next() && !isCancelled()) {
                                rowTotal = rs.getInt("rowTotal");

                                prbMigracaoLinhas.setMaximum(rowTotal);

                                row++;
                                rowCount++;

                                prbMigracaoLinhas.setValue(rowCount);
                                prbMigracaoLinhas.setString((rowCount) + " de " + rowTotal + " (" + (rowCount * 100) / rowTotal + "%)");

                                sqlRow.append(tabela.toSQLInsert(rs).toString());

                                if (row == Integer.parseInt(txtLinhasBatch.getText()) || rowCount == rowTotal) {
                                    stmtPara.close();
                                    switch (direcao) {
                                        case SQLServer:
                                            stmtPara = connSyBase9.createStatement();
                                            break;
                                        case SyBase9:
                                            stmtPara = connSQLServer.createStatement();
                                            break;
                                    }
                                    stmtPara.executeUpdate(sqlRow.toString());

                                    sqlRow = new StringBuilder();

                                    row = 0;
                                }
                            }
                        } else {
                            swMig.cancel(true);
                            txaMigracao.append("Erro: Problemas ao retornar os dados!\n");
                            txaMigracao.setCaretPosition(txaMigracao.getDocument().getLength());
                        }
                    } catch (SQLException e) {
                        if (!swMig.isCancelled()) {
                            txaMigracao.append("Erro: " + e.getMessage() + "\n" + "SQL: " + sql + "\n");
                            txaMigracao.setCaretPosition(txaMigracao.getDocument().getLength());

                            System.out.println(sqlRow.toString());
                        }
                    } finally {
                        if (rs != null) rs.close();
                        if (stmtDe != null) stmtDe.close();
                        if (stmtPara != null) stmtPara.close();
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

    private void changeDirecao() {
        direcao = (FontDad) cbxDirecao.getSelectedItem();
        if (direcao != null) {
            switch (direcao) {
                case SQLServer:
                    tgbSQLServer.setBounds(10, 45, 125, 23);
                    tgbSyBase9.setBounds(10, 79, 125, 23);
                    break;
                case SyBase9:
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
                        + txtSQLServerUsuario.getText() + ";Password=" + (new String(txtSQLServerSenha.getPassword()));

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
