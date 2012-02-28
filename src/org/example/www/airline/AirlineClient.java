package org.example.www.airline;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

public class AirlineClient extends JFrame {
	// Date
	private JTextField dateField = null;
	private JLabel dateLabel = null;
	// Departure
	private JTextField departureField = null;
	private JLabel departureLabel = null;
	// Destination
	private JTextField destinationField = null;
	private JLabel destinationLabel = null;
	// Number of persiosn
	private JTextField personsField = null;
	private JLabel personsLabel = null;
	// Flightnumber
	private JTextField flightnumberField = null;
	private JLabel flightnumberLabel = null;

	// Result
	private JTextField resultField = null;
	private JLabel resultLabel = null;
	// URL
	private JLabel urlLabel = null;
	private JTextField urlField = null;
	private final String defaultUrl = "http://localhost:8080/SOA_-_Assignment_1/services/airline";
	private String url;
	// Buttons
	private JButton checkButton = null;
	private JButton bookButton = null;

	/**
	 * @param args
	 * @throws AxisFault
	 */
	public static void main(String[] args) throws AxisFault {
		new AirlineClient();
	}

	/**
	 * Constructor
	 * 
	 * @throws AxisFault
	 */
	public AirlineClient() throws AxisFault {
		super("Airline Client");
		wsInit(defaultUrl);
		initialize();
	}

	public AirlineStub stub;

	public void wsInit(String endpoint) throws AxisFault {
		stub = new AirlineStub(endpoint);
	}

	private void initialize() {
		Container c = this.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

		dateLabel = new JLabel();
		dateLabel.setText("Date:");
		dateField = new JTextField(10);

		departureLabel = new JLabel();
		departureLabel.setText("Departure:");
		departureField = new JTextField(10);

		destinationLabel = new JLabel();
		destinationLabel.setText("Destination:");
		destinationField = new JTextField(10);

		personsLabel = new JLabel();
		personsLabel.setText("Number of persons:");
		personsField = new JTextField(10);

		resultLabel = new JLabel();
		resultLabel.setText("Result:");
		resultField = new JTextField(10);

		flightnumberLabel = new JLabel();
		flightnumberLabel.setText("Flightnumber:");
		flightnumberField = new JTextField(10);

		urlLabel = new JLabel();
		urlLabel.setText("URL:");

		checkButton = new JButton();
		checkButton.setText("Get flightnumbers");
		bookButton = new JButton();
		bookButton.setText("Book flight");

		url = defaultUrl;

		checkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// update endpoint URL with the value of urlfield
				url = urlField.getText();
				try {
					stub = new AirlineStub(url);
					AirlineStub.FlightRequest request = new AirlineStub.FlightRequest();
					AirlineStub.FlightNumberList response;
					request.setDate(dateField.getText());
					request.setDeparture(departureField.getText());
					request.setDestination(destinationField.getText());
					request.setNumberOfPersons(new BigInteger(personsField
							.getText()));

					response = stub.checkAvailability(request);
					String result = "";
					for (String str : response.getFlightNumber()) {
						result = result.concat(str + " ");
					}
					resultField.setText(result);

				} catch (AxisFault e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		bookButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// update endpoint URL with the value of urlfield
				url = urlField.getText();
				try {

					AirlineStub.BookingRequest request = new AirlineStub.BookingRequest();
					AirlineStub.BookingResponse response;
					request.setDate(dateField.getText());
					request.setFlightNumber(flightnumberField.getText());
					request.setNumberOfPersons(new BigInteger(personsField.getText()));
					
					response = stub.bookFlight(request);
					resultField.setText(response.getBookingResponse());
				} catch (AxisFault e1) {
					e1.printStackTrace();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});

		urlField = new JTextField(url);
		urlField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == urlField) {
					try {
						stub = new AirlineStub(url);
					} catch (AxisFault e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		JPanel infoPanel = new JPanel();
		infoPanel.add(dateLabel);
		infoPanel.add(dateField);
		infoPanel.add(personsLabel);
		infoPanel.add(personsField);
		c.add(infoPanel);

		
		JPanel checkPanel = new JPanel();
		checkPanel.add(departureLabel);
		checkPanel.add(departureField);
		checkPanel.add(destinationLabel);
		checkPanel.add(destinationField);
		checkPanel.add(checkButton);
		c.add(checkPanel);
		
		JPanel bookPanel = new JPanel();
		bookPanel.add(flightnumberLabel);
		bookPanel.add(flightnumberField);
		bookPanel.add(bookButton);
		c.add(bookPanel);
		
		JPanel resultPanel = new JPanel();
		resultPanel.add(resultLabel);
		resultPanel.add(resultField);
		c.add(resultPanel);

		JPanel urlPanel = new JPanel();
		urlPanel.add(urlLabel);
		urlPanel.add(urlField);
		c.add(urlPanel);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.setSize(650, 250);
		this.setVisible(true);
	}
}
