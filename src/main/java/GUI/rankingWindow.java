package GUI;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.TableRowSorter;

public class rankingWindow {

	/**
	 * Costruttore che si occupa di generare, a partire da un arrayList di
	 * stringhe, la classifica generale.
	 */
	public rankingWindow(Object[][] ranking) {

		MyTableModel tblModel = new MyTableModel(ranking);
		JTable table = new JTable(tblModel);
		// JTable table = new JTable(data, columnNames);
		table.setEnabled(false);
		table.setPreferredScrollableViewportSize(new Dimension(500, 180));
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);

		new Comparator<Short>() {
			@Override
			public int compare(Short o1, Short o2) {
				short i1 = o1.shortValue();
				short i2 = o2.shortValue();
				if (i1 < i2) {
					return -1;
				}
				if (i1 > i2) {
					return 1;
				}

				return o1.compareTo(o2);
			}
		};

		TableRowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(
				tblModel);

		table.setRowSorter(sorter);

		JScrollPane scrollPane = new JScrollPane(table);

		JFrame frame = new JFrame("Classifica");

		frame.add(scrollPane);

		frame.pack();

		frame.setLocationRelativeTo(null);

		frame.setVisible(true);

	}

}
