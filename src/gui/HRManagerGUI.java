package gui;

import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class HRManagerGUI extends JFrame {
    private JTextField txtNV_MaNV, txtNV_TenNV, txtNV_ChucVu, txtNV_Luong, txtNV_MaPB, txtNV_TenPB;
    private JTextField txtTimKiem;
    private JComboBox<String> cbLocChucVu;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnXuatXML;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private NhanVienDAO dao;
    private ThongKePanel thongKePanel; 
    
    private JTable tablePB;
    private DefaultTableModel modelPB;
    private TableRowSorter<DefaultTableModel> sorterPB;
    private JTextField txtPB_MaPB, txtPB_TenPB, txtTimKiemPB; 
    private PhongBanDAO pbDao = new PhongBanDAO();

    private String getValueSafe(int row, int col) {
        Object val = tableModel.getValueAt(row, col);
        return (val != null) ? val.toString() : "";
    }

    public HRManagerGUI() {
        dao = new NhanVienDAO();
        setTitle("Hệ thống quản lí nhân sự công ty");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(240, 240, 240));
        
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {}
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(isSelected ? Color.WHITE : new Color(245, 245, 245));
                g.fillRect(x, y, w, h);
            }
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(new Color(210, 210, 210));
                g.drawRect(x, y, w, h);
                if (isSelected) {
                    g.setColor(Color.WHITE);
                    g.drawLine(x + 1, y + h, x + w - 1, y + h);
                    g.setColor(new Color(41, 128, 185));
                    g.fillRect(x, y, w, 3);
                }
            }
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                int w = tabbedPane.getWidth();
                int h = tabbedPane.getHeight();
                g.setColor(new Color(210, 210, 210));
                g.drawRect(0, 0, w - 1, h - 1);
            }
        });

        tabbedPane.addTab("Quản lý nhân viên", createQuanLyPanel());
        tabbedPane.addTab("Quản lý phòng ban", createPhongBanPanel());
        
        thongKePanel = new ThongKePanel();
        tabbedPane.addTab("Dashboard thống kê", thongKePanel);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) thongKePanel.loadData();
        });

        add(tabbedPane);
        performBackgroundSync();
        loadDataPB();
        refreshLocChucVu();
    }

    private JPanel createQuanLyPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelInputWrapper = new JPanel(new BorderLayout());
        panelInputWrapper.setBackground(Color.WHITE);
        panelInputWrapper.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            " Thông tin nhân viên ", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)));

        JPanel panelInput = new JPanel(new GridLayout(6, 2, 20, 15));
        panelInput.setBackground(Color.WHITE);
        panelInput.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        txtNV_MaNV = new JTextField();
        txtNV_MaNV.setEditable(false);
        txtNV_MaNV.setEnabled(false);
        txtNV_MaNV.setDisabledTextColor(Color.DARK_GRAY);
        txtNV_MaNV.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNV_MaNV.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtNV_MaNV.setBackground(new Color(240, 240, 240));

        txtNV_TenNV = createStyledTextField();
        txtNV_ChucVu = createStyledTextField();
        txtNV_Luong = createStyledTextField();
        txtNV_MaPB = createStyledTextField();
        
        txtNV_TenPB = createStyledTextField();
        txtNV_TenPB.setEditable(false);
        txtNV_TenPB.setEnabled(false);
        txtNV_TenPB.setDisabledTextColor(Color.BLACK);

        panelInput.add(createStyledLabel("Mã nhân viên:")); panelInput.add(txtNV_MaNV);
        panelInput.add(createStyledLabel("Họ tên:")); panelInput.add(txtNV_TenNV);
        panelInput.add(createStyledLabel("Chức vụ:")); panelInput.add(txtNV_ChucVu);
        panelInput.add(createStyledLabel("Mức lương:")); panelInput.add(txtNV_Luong);
        panelInput.add(createStyledLabel("Mã phòng ban:")); panelInput.add(txtNV_MaPB);
        panelInput.add(createStyledLabel("Tên phòng ban:")); panelInput.add(txtNV_TenPB);
        
        panelInputWrapper.add(panelInput, BorderLayout.CENTER);
        panel.add(panelInputWrapper, BorderLayout.NORTH);

        JPanel mainCenter = new JPanel(new BorderLayout(10, 10));
        mainCenter.setBackground(Color.WHITE);

        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFilter.setBackground(Color.WHITE);
        txtTimKiem = new JTextField(15);
        cbLocChucVu = new JComboBox<>();
        
        panelFilter.add(new JLabel("Tìm theo tên: "));
        panelFilter.add(txtTimKiem);
        panelFilter.add(new JLabel("  Lọc chức vụ: "));
        panelFilter.add(cbLocChucVu);
        mainCenter.add(panelFilter, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Mã NV", "Họ tên", "Chức vụ", "Lương", "Mã PB", "Tên PB"}, 0);
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(235, 245, 251));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 38));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225)));
        mainCenter.add(scrollPane, BorderLayout.CENTER);
        panel.add(mainCenter, BorderLayout.CENTER);

        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { applyFilter(); }
        });
        cbLocChucVu.addActionListener(e -> applyFilter());

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelButtons.setBackground(Color.WHITE);
        
        btnThem = styleButton("Thêm", new Color(46, 204, 113));
        btnSua = styleButton("Sửa", new Color(241, 196, 15));
        btnXoa = styleButton("Xóa", new Color(231, 76, 60));
        btnLamMoi = styleButton("Làm mới", new Color(52, 152, 219));
        btnXuatXML = styleButton("Xuất XML", new Color(155, 89, 182));
        
        panelButtons.add(btnThem); panelButtons.add(btnSua); 
        panelButtons.add(btnXoa); panelButtons.add(btnLamMoi);
        panelButtons.add(btnXuatXML);
        panel.add(panelButtons, BorderLayout.SOUTH);

        addEvents(); 
        return panel;
    }

    private JPanel createPhongBanPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel northPanel = new JPanel(new GridBagLayout());
        northPanel.setBackground(Color.WHITE);
        northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtPB_MaPB = createStyledTextField();
        txtPB_TenPB = createStyledTextField();
        txtTimKiemPB = createStyledTextField();
        
        JButton btnThemPB = styleButton("Thêm", new Color(46, 204, 113));
        JButton btnXoaPB = styleButton("Xóa", new Color(231, 76, 60));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        northPanel.add(new JLabel("Mã phòng ban: "), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.3;
        northPanel.add(txtPB_MaPB, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0;
        northPanel.add(new JLabel(" Tên phòng ban: "), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.7;
        northPanel.add(txtPB_TenPB, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0;
        northPanel.add(btnThemPB, gbc);
        
        gbc.gridx = 5; gbc.weightx = 0;
        northPanel.add(btnXoaPB, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        northPanel.add(new JLabel("Tìm kiếm phòng ban: "), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 5; gbc.weightx = 1.0;
        northPanel.add(txtTimKiemPB, gbc);

        modelPB = new DefaultTableModel(new String[]{"Mã phòng ban", "Tên phòng ban"}, 0);
        tablePB = new JTable(modelPB);
        tablePB.setRowHeight(32);
        
        sorterPB = new TableRowSorter<>(modelPB);
        tablePB.setRowSorter(sorterPB);

        JTableHeader headerPB = tablePB.getTableHeader();
        headerPB.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPB.setBackground(new Color(41, 128, 185));
        headerPB.setForeground(Color.WHITE);
        headerPB.setPreferredSize(new Dimension(100, 38));
        
        tablePB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablePB.setSelectionBackground(new Color(235, 245, 251));
        tablePB.setSelectionForeground(Color.BLACK);
        tablePB.setShowGrid(true);
        tablePB.setGridColor(new Color(230, 230, 230));

        btnThemPB.addActionListener(e -> {
            try {
                pbDao.insert(txtPB_MaPB.getText().trim(), txtPB_TenPB.getText().trim());
                loadDataPB();
                txtPB_MaPB.setText(""); txtPB_TenPB.setText("");
                JOptionPane.showMessageDialog(this, "Đã thêm phòng ban thành công!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });
        
        btnXoaPB.addActionListener(e -> {
            int row = tablePB.getSelectedRow();
            if (row == -1) return;
            try {
                int modelRow = tablePB.convertRowIndexToModel(row);
                String maPB = modelPB.getValueAt(modelRow, 0).toString();
                pbDao.delete(maPB);
                loadDataPB();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });
        
        txtTimKiemPB.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String text = txtTimKiemPB.getText().trim();
                if (text.length() == 0) {
                    sorterPB.setRowFilter(null);
                } else {
                    sorterPB.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
                }
            }
        });
        
        panel.add(northPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPanePB = new JScrollPane(tablePB);
        scrollPanePB.getViewport().setBackground(Color.WHITE);
        scrollPanePB.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225)));
        panel.add(scrollPanePB, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadDataPB() {
        modelPB.setRowCount(0);
        try {
            for (PhongBan pb : pbDao.getAllPhongBan()) {
                modelPB.addRow(new Object[]{pb.getMaPB(), pb.getTenPB()});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void refreshLocChucVu() {
        cbLocChucVu.removeAllItems();
        cbLocChucVu.addItem("Tất cả chức vụ");
        for (String cv : dao.getChucVuDuyNhat()) {
            cbLocChucVu.addItem(cv);
        }
    }

    private void applyFilter() {
        RowFilter<DefaultTableModel, Object> rf = RowFilter.andFilter(java.util.Arrays.asList(
            RowFilter.regexFilter("(?i)" + txtTimKiem.getText(), 1),
            cbLocChucVu.getSelectedIndex() == 0 ? RowFilter.regexFilter("") : RowFilter.regexFilter("^" + cbLocChucVu.getSelectedItem() + "$", 2)
        ));
        sorter.setRowFilter(rf);
    }

    private JLabel createStyledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }

    private JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return txt;
    }

    private JButton styleButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBorderPainted(false);
        return btn;
    }

    private void addEvents() {
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);
                txtNV_MaNV.setText(getValueSafe(modelRow, 0));
                txtNV_TenNV.setText(getValueSafe(modelRow, 1));
                txtNV_ChucVu.setText(getValueSafe(modelRow, 2));
                txtNV_Luong.setText(getValueSafe(modelRow, 3).replaceAll("[^0-9]", ""));
                txtNV_MaPB.setText(getValueSafe(modelRow, 4));
                txtNV_TenPB.setText(getValueSafe(modelRow, 5));
            }
        });

        txtNV_MaPB.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String maPB = txtNV_MaPB.getText().trim();
                if (!maPB.isEmpty()) {
                    boolean checked = false;
                    try {
                        for (PhongBan pb : pbDao.getAllPhongBan()) {
                            if (pb.getMaPB().equalsIgnoreCase(maPB)) {
                                txtNV_TenPB.setEnabled(true);
                                txtNV_TenPB.setText(pb.getTenPB());
                                txtNV_TenPB.setEnabled(false);
                                checked = true;
                                break;
                            }
                        }
                        if (!checked) {
                            txtNV_TenPB.setEnabled(true);
                            txtNV_TenPB.setText("");
                            txtNV_TenPB.setEnabled(false);
                            JOptionPane.showMessageDialog(null, "Mã phòng ban này không tồn tại!");
                        }
                    } catch (Exception ex) { ex.printStackTrace(); }
                } else {
                    txtNV_TenPB.setEnabled(true);
                    txtNV_TenPB.setText("");
                    txtNV_TenPB.setEnabled(false);
                }
            }
        });

        btnLamMoi.addActionListener(e -> {
            clearForm();
            performBackgroundSync();
            loadDataPB();
        });

        btnThem.addActionListener(e -> {
            try {
                String tenNV = txtNV_TenNV.getText().trim();
                String chucVu = txtNV_ChucVu.getText().trim();
                String luongStr = txtNV_Luong.getText().trim().replaceAll("[^0-9]", "");
                String maPB = txtNV_MaPB.getText().trim();
                String tenPB = txtNV_TenPB.getText().trim();

                if (tenNV.isEmpty() || maPB.isEmpty() || tenPB.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã phòng ban hợp lệ trước khi Thêm!");
                    return;
                }

                double luong = Double.parseDouble(luongStr);
                NhanVien nv = new NhanVien(0, tenNV, chucVu, luong, maPB, tenPB);
                dao.insert(nv);
                
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                performBackgroundSync();
                clearForm(); 
                thongKePanel.loadData(); 
                refreshLocChucVu();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mức lương nhập vào không hợp lệ!");
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage()); 
            }
        });

        btnSua.addActionListener(e -> {
            try {
                int maNV = Integer.parseInt(txtNV_MaNV.getText().trim());
                String tenNV = txtNV_TenNV.getText().trim();
                String chucVu = txtNV_ChucVu.getText().trim();
                String luongStr = txtNV_Luong.getText().trim().replaceAll("[^0-9]", "");
                String maPB = txtNV_MaPB.getText().trim();
                String tenPB = txtNV_TenPB.getText().trim();

                if (tenPB.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mã phòng ban không hợp lệ!");
                    return;
                }

                double luong = Double.parseDouble(luongStr);
                NhanVien nv = new NhanVien(maNV, tenNV, chucVu, luong, maPB, tenPB);
                dao.update(nv);
                
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                performBackgroundSync();
                thongKePanel.loadData(); 
                refreshLocChucVu();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + ex.getMessage()); 
            }
        });

        btnXoa.addActionListener(e -> {
            try {
                dao.delete(Integer.parseInt(txtNV_MaNV.getText()));
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                performBackgroundSync();
                clearForm(); 
                thongKePanel.loadData(); 
                refreshLocChucVu();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });

        btnXuatXML.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file XML bảng lương");
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String path = fileToSave.getAbsolutePath();
                if (!path.toLowerCase().endsWith(".xml")) {
                    path += ".xml";
                }
                
                try {
                    List<NhanVien> list = dao.getAllNhanVien();
                    util.FileExportHelper.exportBangLuongToXML(list, path);
                    JOptionPane.showMessageDialog(this, "Xuất file XML bảng lương thành công!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file XML: " + ex.getMessage());
                }
            }
        });
    }

    public void performBackgroundSync() {
        SwingWorker<List<NhanVien>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<NhanVien> doInBackground() throws Exception {
                return dao.getAllNhanVien();
            }

            @Override
            protected void done() {
                try {
                    List<NhanVien> list = get();
                    tableModel.setRowCount(0);
                    
                    DecimalFormat df = new DecimalFormat("#,### VNĐ");
                    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                    dfs.setGroupingSeparator('.'); 
                    df.setDecimalFormatSymbols(dfs);

                    for (NhanVien nv : list) {
                        tableModel.addRow(new Object[]{
                            nv.getMaNV(), 
                            nv.getTenNV(), 
                            nv.getChucVu(), 
                            df.format(nv.getLuong()), 
                            nv.getMaPB(), 
                            nv.getTenPB()
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    private void clearForm() {
        txtNV_MaNV.setText(""); txtNV_TenNV.setText(""); txtNV_ChucVu.setText(""); txtNV_Luong.setText("");
        txtNV_MaPB.setText(""); txtNV_TenPB.setText("");
        table.clearSelection();
    }
}