package gui;

import dao.ThongKeDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class ThongKePanel extends JPanel {
    private ThongKeDAO thongKeDAO;
    private JLabel lblTongNhanVien, lblTongQuyLuong;
    private JPanel chartContainer;

    public ThongKePanel() {
        thongKeDAO = new ThongKeDAO();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelOverview = new JPanel(new GridLayout(1, 2, 20, 0));
        
        lblTongNhanVien = new JLabel("Tổng nhân sự: 0", SwingConstants.CENTER);
        lblTongNhanVien.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongNhanVien.setOpaque(true);
        lblTongNhanVien.setBackground(new Color(173, 216, 230));
        
        lblTongQuyLuong = new JLabel("Tổng quỹ lương: 0 ₫", SwingConstants.CENTER);
        lblTongQuyLuong.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongQuyLuong.setOpaque(true);
        lblTongQuyLuong.setBackground(new Color(144, 238, 144));

        panelOverview.add(lblTongNhanVien);
        panelOverview.add(lblTongQuyLuong);
        panelOverview.setPreferredSize(new Dimension(0, 80));
        add(panelOverview, BorderLayout.NORTH);

        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(chartContainer, BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Cập nhật lại thống kê");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> loadData());
        add(btnRefresh, BorderLayout.SOUTH);

        loadData();
    }

    public void loadData() {
        int tongNV = thongKeDAO.getTongSoNhanVien();
        double tongLuong = thongKeDAO.getTongQuyLuong();

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

        lblTongNhanVien.setText("Tổng nhân sự: " + tongNV + " người");
        lblTongQuyLuong.setText("Tổng quỹ lương: " + currencyVN.format(tongLuong));

        chartContainer.removeAll();
        if (tongNV > 0) {
            chartContainer.add(createPieChart(), BorderLayout.CENTER);
        } else {
            chartContainer.add(new JLabel("Chưa có dữ liệu nhân viên để thống kê", SwingConstants.CENTER));
        }
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private ChartPanel createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Double> mapLuong = thongKeDAO.getThongKeLuongTheoChucVu();

        for (Map.Entry<String, Double> entry : mapLuong.entrySet()) {
            if (entry.getValue() > 0) {
                dataset.setValue(entry.getKey(), entry.getValue());
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Tỷ trọng quỹ lương theo chức vụ",
                dataset,
                true,
                true,
                false
        );

        return new ChartPanel(chart);
    }
}