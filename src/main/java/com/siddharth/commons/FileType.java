package com.siddharth.commons;

public enum FileType {
	
	pdf("pdf"),
	doc("doc"),
	docx("docx"),
	ppt("ppt"),
	pptx("pptx"),
	odt("odt"),
	png("png"),
	jpeg("jpeg"),
	jpg("jpg"),
	invalid("invalid");

	
	private String fileType = null;
	
	private FileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public static FileType getEnum(String fileType)
	{
		for(FileType enumType : FileType.values())
		{
			if(fileType.equalsIgnoreCase(enumType.name())
					|| fileType.contains(enumType.name()))
				return enumType;
		}
		return FileType.invalid;
	}

}
