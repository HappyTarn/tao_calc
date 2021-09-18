package google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Create;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.DeleteSheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import taoCalc.dto.Summary;

public class GoogleSheets {

	final static String spreadsheetId = "1rq0w5s_AUNcssgoAh2HMcnWKuQNFa09QpE0fvdU0Mhc";

	/**
	 * Prints the names and majors of students in a sample spreadsheet:
	 * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
	 * @param event 
	 */
	public static String create(List<Summary> list, ButtonClickEvent event) {
		String result = "";
		try {
			Sheets service = SheetsServiceUtil.getSheetsService();
			Spreadsheet spreadsheet = new Spreadsheet().setProperties(new SpreadsheetProperties().setTitle("TaoCalc"));

			NumberFormat nfNum = NumberFormat.getNumberInstance();
			List<List<Object>> lists = new ArrayList<List<Object>>();
			lists.add(Arrays.asList("名前", "戦闘", "地上げ", "武器", "素材", "武器魂"));
			for (Summary summary : list) {
				List<Object> l = new ArrayList<Object>();
				l.add(summary.getMemberName());
				l.add(nfNum.format(summary.getCombatCount()));
				l.add(nfNum.format(summary.getGroundCount()));
				l.add(nfNum.format(summary.getWeaponCount()));
				l.add(nfNum.format(summary.getSozaiCount()));
				l.add(nfNum.format(summary.getBukikonCount()));
				lists.add(l);
			}
			ValueRange body = new ValueRange()
					.setValues(lists);

			Create create = service.spreadsheets()
					.create(spreadsheet);
			spreadsheet = create.execute();
			service.spreadsheets().values()
					.update(spreadsheet.getSpreadsheetId(), "A1", body)
					.setValueInputOption("RAW")
					.execute();
			System.out.println(spreadsheet.getSpreadsheetUrl());

			Drive driveService = DriveServiceUtil.getSheetsService();
			File file = driveService.files().get(spreadsheet.getSpreadsheetId())
					.setFields("parents")
					.execute();
			StringBuilder previousParents = new StringBuilder();
			for (String parent : file.getParents()) {
				previousParents.append(parent);
				previousParents.append(',');
			}
			driveService.files().update(spreadsheet.getSpreadsheetId(), null)
					.setAddParents("1FBiaNNXHDVAMK1p3s17ujwRj45IgQTL2")
					.setRemoveParents(previousParents.toString())
					.setFields("id, parents")
					.execute();
			result = spreadsheet.getSpreadsheetUrl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public static void deleteSheet(Integer sheetId, Sheets service) throws IOException, GeneralSecurityException {
		List<Request> requests = new ArrayList<>();
		requests.add(new Request().setDeleteSheet(new DeleteSheetRequest().setSheetId(sheetId)));

		BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);

		service.spreadsheets().batchUpdate(spreadsheetId, body).execute();
	}

	public static void addSheet(Integer sheetId, String title, Sheets service)
			throws IOException, GeneralSecurityException {

		AddSheetRequest addSheetRequest = new AddSheetRequest()
				.setProperties(new SheetProperties().setSheetId(sheetId).setTitle(title));

		List<Request> requests = new ArrayList<>();
		requests.add(new Request().setAddSheet(addSheetRequest));

		BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);

		service.spreadsheets().batchUpdate(spreadsheetId, body).execute();
	}
}