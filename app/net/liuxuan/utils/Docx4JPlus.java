package net.liuxuan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBException;

import models.VersionControl;
 
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Document;

public class Docx4JPlus {
	public static String Test(VersionControl zf) throws Docx4JException,
			JAXBException {
		if (zf == null) {
			return "";
		}
		
		String templatefile = "d://tempfile/TEMPLATE.DOCX";
		// Open a document from the file system
		// 1. Load the Package
		WordprocessingMLPackage wordMLPackageMB = WordprocessingMLPackage
				.load(new java.io.File(templatefile));
		WordprocessingMLPackage wordMLPackageout = WordprocessingMLPackage
				.createPackage();
		// 2. Fetch the document part
		MainDocumentPart documentPartMB = wordMLPackageMB.getMainDocumentPart();
		MainDocumentPart documentPartout = wordMLPackageout
				.getMainDocumentPart();

		org.docx4j.wml.Document wmlDocumentElMB = (org.docx4j.wml.Document) documentPartMB
				.getJaxbElement();
		// xml --> string
		String xmlMB = XmlUtils.marshaltoString(wmlDocumentElMB, true);
		
		SimpleDateFormat sdateformatter = new SimpleDateFormat("yyyy-MM-dd");
		
		xmlMB = xmlMB.replaceAll("FILENAME", zf.device)
				.replaceAll("LANGUAGE", zf.language)
				.replaceAll("FILEVERSION", zf.versionno)
				.replaceAll("PUBLISHDATE", sdateformatter.format(new Date()))
				.replaceAll("MODIFYDATE", zf.modifydate)
				.replaceAll("MODIFYREASON", zf.modifyreason)
				.replaceAll("MODIFIER", zf.modifier)
				.replaceAll("CURRENTVERSION", zf.versionno)
				.replaceAll("FATHERVERSION", zf.fatherversionno)
				.replaceAll("MODIFYCONTENT", zf.modifycontent)
				.replaceAll("VERSIONTYPE", zf.versionkind)
				.replaceAll("PACKAGEFILES", zf.packagecontent);

//		System.out.println(xmlMB);
		Object obj = XmlUtils.unmarshalString(xmlMB);
		documentPartout.setJaxbElement((Document) obj);

		String pathfile = "d:/tempfile/" + System.currentTimeMillis() + ".docx";
		new SaveToZipFile(wordMLPackageout).save(pathfile);
		return pathfile;
	}

	public static void main(String[] args) {
		try {
			Test(null);
		} catch (Docx4JException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}