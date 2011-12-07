package ru.oculus.database.service.metadata;

public class SchemaMetadata {
	public static final SchemaMetadata NO_METADATA = new SchemaMetadata();

	private String description;
	private String installationDate;
	private String dumpDate;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstallationDate() {
		return installationDate;
	}

	public void setInstallationDate(String installationDate) {
		this.installationDate = installationDate;
	}

	public String getDumpDate() {
		return dumpDate;
	}

	public void setDumpDate(String dumpDate) {
		this.dumpDate = dumpDate;
	}
}
