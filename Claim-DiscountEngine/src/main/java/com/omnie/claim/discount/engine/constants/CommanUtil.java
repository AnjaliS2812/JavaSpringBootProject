package com.omnie.claim.discount.engine.constants;

import static com.omnie.claim.discount.engine.constants.Constants.AM01;
import static com.omnie.claim.discount.engine.constants.Constants.C1;
import static com.omnie.claim.discount.engine.constants.Constants.CA;
import static com.omnie.claim.discount.engine.constants.Constants.CB;
import static com.omnie.claim.discount.engine.constants.Constants.COMMA;
import static com.omnie.claim.discount.engine.constants.Constants.D2;
import static com.omnie.claim.discount.engine.constants.Constants.EMPTY_STR;
import static com.omnie.claim.discount.engine.constants.Constants.H_HMMSS_SSS;
import static com.omnie.claim.discount.engine.constants.Constants.TP;
import static com.omnie.claim.discount.engine.constants.Constants.YYYY_MM_DD;
import static com.omnie.claim.discount.engine.constants.Constants.YYYY_M_MDD;
import static com.omnie.claim.discount.engine.constants.Constants._D0;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.LoggerFactory;

/**
 * A Utility class responsible for doing common task
 * 
 * @author Ram Kishor
 *
 */
public class CommanUtil {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(CommanUtil.class);

	public static String strTrim(String strTrim) {
		return CommanUtil.strNullCheck(strTrim).trim();
	}

	public static String strNullCheck(String nullCheck) {
		return nullCheck == null ? EMPTY_STR : nullCheck;
	}

	public static String getBinNumberFromMessage(String message) {
		return message.substring(1, 7);
	}

	public static String getProcessorControlNumberFromMessage(String message) {
		return message.substring(11, 21).trim();
	}

	public static String getDateOfServicFromMessage(String message) {
		return message.substring(38, 47).trim();
	}

	public static String removeSpecialCharacters(String message) {
		byte input1 = 0x1C; // FS Field Seperator
		char DelimeterCharactersFS = (char) (input1 & 0xFF);

		byte input2 = 0x1D; // GS Group Separatot
		char DelimeterCharactersGS = (char) (input2 & 0xFF);

		byte input3 = 0x1E;// RS Region Separator
		char DelimeterCharactersRS = (char) (input3 & 0xFF);

		byte input4 = 0x02; // STX
		char DelimeterCharactersSTX = (char) (input4 & 0xFF);

		byte input5 = 0x03; // ETX
		char DelimeterCharactersETX = (char) (input5 & 0xFF);

		int length = 1;
		int StartingPositionGS = message.indexOf(DelimeterCharactersGS);

		char replaceCharacter = ',';
		char blankCharacter = ' ';

		for (int i = 0; i < message.length(); i++) {
			if ((StartingPositionGS + length) <= message.length()) {
				message = message.replace(DelimeterCharactersGS, replaceCharacter);
				message = message.replace(DelimeterCharactersRS, replaceCharacter);
				message = message.replace(DelimeterCharactersFS, replaceCharacter);
				message = message.replace(DelimeterCharactersSTX, blankCharacter);
				message = message.replace(DelimeterCharactersETX, blankCharacter);
			}
		}
		return message.trim();
	}

	public static String getRxNumber(String message) {
		message = removeSpecialCharacters(message);
		String[] myMessage = message.split(COMMA);
		String servicereReferenceNumber = EMPTY_STR;
		String orignalD2Value = EMPTY_STR;
		for (String field : myMessage) {
			if (field.equals(EMPTY_STR)) {
				continue;
			}
			if (field.substring(0, 2).equals(D2)) {
				servicereReferenceNumber = field.substring(0);
				break;
			}
		}
		if (!servicereReferenceNumber.equals(EMPTY_STR) && servicereReferenceNumber != null) {
			orignalD2Value = servicereReferenceNumber.substring(2);
		}
		return orignalD2Value;
	}

	public static String getGroupNumber(String message) {
		message = removeSpecialCharacters(message);
		String[] myMessage = message.split(COMMA);
		String groupNumber = EMPTY_STR;
		String orignalC1Value = EMPTY_STR;
		for (String field : myMessage) {
			if (field.equals(EMPTY_STR)) {
				continue;
			}
			if (field.substring(0, 2).equals(C1)) {
				groupNumber = field.substring(0);
				break;
			}
		}
		if (!groupNumber.equals(EMPTY_STR) && groupNumber != null) {
			orignalC1Value = groupNumber.substring(2);
		}
		return orignalC1Value;
	}

	public static java.sql.Date parseDate(String dateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(YYYY_M_MDD);
		try {
			Date date = formatter.parse(dateStr);
			return new java.sql.Date(date.getTime());
		} catch (ParseException e) {
			logger.error("Error in Parsing Date");
		}
		return null;
	}

	public static String attachTimeStampOnFirstName(String message) {
		byte input1 = 0x1C; // FS Field Seperator
		char DelimeterCharactersFS = (char) (input1 & 0xFF);

		byte input2 = 0x1D; // GS Group Separatot
		char DelimeterCharactersGS = (char) (input2 & 0xFF);

		byte input3 = 0x1E;// RS Region Separator
		char DelimeterCharactersRS = (char) (input3 & 0xFF);

		String originrequest1 = message.substring(0, message
				.indexOf(Character.toString(DelimeterCharactersRS) + Character.toString(DelimeterCharactersFS) + AM01));
		String request1 = message.substring(message
				.indexOf(Character.toString(DelimeterCharactersRS) + Character.toString(DelimeterCharactersFS) + AM01));
		String originrequest2 = request1.substring(request1.indexOf(Character.toString(DelimeterCharactersGS)));
		request1 = request1.substring(
				request1.indexOf(
						Character.toString(DelimeterCharactersRS) + Character.toString(DelimeterCharactersFS) + AM01),
				request1.indexOf(Character.toString(DelimeterCharactersGS)));

		int index = request1.indexOf(Character.toString(DelimeterCharactersFS) + CA);
		String request4 = request1.substring(0, index + 3);
		String request2 = request1.substring(index + 3);
		String request3 = request2.substring(request2.indexOf(Character.toString(DelimeterCharactersFS)));
		String originalString4 = request4 + TP + new SimpleDateFormat(H_HMMSS_SSS).format(new java.util.Date())
				+ request3;
		return originrequest1 + originalString4 + originrequest2;
	}

	public static String attachTimeStampOnLastName(String message) {
		byte input1 = 0x1C; // FS Field Seperator
		char DelimeterCharactersFS = (char) (input1 & 0xFF);
		int index = message.indexOf(Character.toString(DelimeterCharactersFS) + CB);
		String request4 = message.substring(0, index + 3);
		String request2 = message.substring(index + 3);
		String request3 = request2.substring(request2.indexOf(Character.toString(DelimeterCharactersFS)));
		return request4 + TP + new SimpleDateFormat(H_HMMSS_SSS).format(new java.util.Date())
				+ request3;
	}

	public static boolean between(Date date, Date dateStart, Date dateEnd) {
		if (date != null && dateStart != null && dateEnd != null) {
			if (date.compareTo(dateStart) >= 0 && date.compareTo(dateEnd) <= 0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static Date stringToDate(String date) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat(YYYY_MM_DD).parse(date));
			Date convertedDate = cal.getTime();
			return convertedDate;
		} catch (Exception e) {
			logger.error("Error in conerting String to date is:--" + e.getMessage());
		}
		return null;
	}

	public static String dateFormate(String formatDate, String inFormate, String outFormate) {
		try {
			SimpleDateFormat sdfSource = new SimpleDateFormat(inFormate);
			Date date = sdfSource.parse(formatDate);
			SimpleDateFormat sdfDestination = new SimpleDateFormat(outFormate);
			formatDate = sdfDestination.format(date);
		} catch (ParseException e) {
			logger.error("Parse Exception in dateFormate-- " + e);
		}
		return formatDate;
	}

	/**
	 * To Check the wrapped request
	 * 
	 * @param message
	 * @return
	 */

	public static boolean isWrappedRequest(String message) {
		byte input1 = 0x1C; // FS Field Seperator
		char DelimeterCharactersFS = (char) (input1 & 0xFF);

		byte input2 = 0x1D; // GS Group Separatot
		char DelimeterCharactersGS = (char) (input2 & 0xFF);

		byte input3 = 0x1E;// RS Region Separator
		char DelimeterCharactersRS = (char) (input3 & 0xFF);

		int indexgs = message.indexOf(Character.toString(DelimeterCharactersGS));
		int indexrs = message.indexOf(Character.toString(DelimeterCharactersRS));
		int indexfs = message.indexOf(Character.toString(DelimeterCharactersFS));
		return (indexgs > 57 && indexrs > 57 && indexfs > 57) ? true : false;
	}

	public static String getDateOfServiceFromWrappedRequest(String message) {
		String strDateOfService = EMPTY_STR;
		if (message.startsWith(_D0)) {
			strDateOfService = message.substring(24 + 39, 32 + 39);
		} else {
			strDateOfService = message.substring(39 + 39, 47 + 39);
		}
		return strDateOfService;
	}

	public static void main(String[] args) {
		System.out.println("\u0003");
		logger.info("originalStrng->"
				+ "009987D0B1TEST      1011629019526     201501150000000000AM04C2TP0009926C1POSTPRICINGC301C61AM01C420100101C51CACARDHOLDERCBTP0009926AM07EM1D210009926E103D700603107558D300D5012D80DE20150115DJ3E70000240000AM11DQ0000180IDU0000180IAM03DBAZ9999999");

		logger.info("updatedString->" + attachTimeStampOnFirstName(
				"009987D0B1TEST      1011629019526     201501150000000000AM04C2TP0009926C1POSTPRICINGC301C61AM01C420100101C51CACARDHOLDERCBTP0009926AM07EM1D210009926E103D700603107558D300D5012D80DE20150115DJ3E70000240000AM11DQ0000180IDU0000180IAM03DBAZ9999999"));

		logger.info("updatedString->" + attachTimeStampOnLastName(
				"0124032   123456                       D0B11A070100886        20150810AM29C419310116CATP151308081CBWHOC71AM04C29999999999C1TXIXAM21ANRF3U14310S434BC01FA1FB54AM22EM1D27777777"));

		System.out.println(new SimpleDateFormat("MMddHHmmssSSS").format(new java.util.Date()));

		String strwrap = "0309032   123456                       017696D0B1TEST      1011629019526     201501150000000000AM04C2TP0009926C1POSTPRICINGC301C61AM01C420100101C51CACARDHOLDERCBTP0009926AM07EM1D210009926E103D700603107558D300D5012D80DE20150115DJ3E70000240000AM11DQ0000180IDU0000180IAM03DBAZ9999999";

		String strwitoutwrap = "009987D0B1TEST      1011629019526     201501150000000000AM04C2TP0009926C1POSTPRICINGC301C61AM01C420100101C51CACARDHOLDERCBTP0009926AM07EM1D210009926E103D700603107558D300D5012D80DE20150115DJ3E70000240000AM11DQ0000180IDU0000180IAM03DBAZ9999999";
		System.out.println(isWrappedRequest(strwrap));

		System.out.println(isWrappedRequest(strwitoutwrap));

		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				System.out.println("******* Updated ********");
			}

		}, 0, 1 * 60000);

	}
}