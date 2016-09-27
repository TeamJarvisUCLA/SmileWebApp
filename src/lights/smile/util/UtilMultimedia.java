package lights.smile.util;

import ve.smile.enums.ExtensionEnum;

public class UtilMultimedia {

	public static ExtensionEnum stringToExtensionEnum(String extensionFile) {

		switch (extensionFile.toLowerCase()) {
		case "png":
			return ExtensionEnum.PNG;
		case "jpg":
			return ExtensionEnum.JPG;
		case "jpeg":
			return ExtensionEnum.JPEG;
		case "pdf":
			return ExtensionEnum.PDF;
		case "doc":
			return ExtensionEnum.DOC;
		case "xlsx":
			return ExtensionEnum.XLSX;
		case "xls":
			return ExtensionEnum.XLS;
		case "zip":
			return ExtensionEnum.ZIP;
		case "rar":
			return ExtensionEnum.RAR;
		case "backup":
			return ExtensionEnum.BACKUP;
		default:
			break;
		}
		return null;
	}

	public static boolean validateFile(String extension) {
		return extension.toUpperCase().equalsIgnoreCase("BACKUP")
				|| extension.toUpperCase().equalsIgnoreCase("SQL")
				|| extension.toUpperCase().equalsIgnoreCase("ZIP")
				|| extension.toUpperCase().equalsIgnoreCase("RAR")
				|| extension.toUpperCase().equalsIgnoreCase("JPG")
				|| extension.toUpperCase().equalsIgnoreCase("PNG")
				|| extension.toUpperCase().equalsIgnoreCase("JPEG")
				|| extension.toUpperCase().equalsIgnoreCase("PDF")
				|| extension.toUpperCase().equalsIgnoreCase("DOC")
				|| extension.toUpperCase().equalsIgnoreCase("DOCX")
				|| extension.toUpperCase().equalsIgnoreCase("XLSX")
				|| extension.toUpperCase().equalsIgnoreCase("XLS");
	}
}