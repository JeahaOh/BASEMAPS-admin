package fi.fta.utils.parse.wfs;

public interface FeatureInfoSpecification
{
	
	public String getPathProvider();
	public String getServiceIdentification();
	public String getTitle();
	public String getPathKeywords();
	public String getPathOperation();
	public String getParameter();
	public String getPathAllowedValue();
	public String getPathExtendedCapabilities();
	public String getSupportedLanguages();
	public String getPathDefaultLanguage();
	public String getSupportedLanguage();
	public String getLanguage();
	public String getPathExtendedMetadataUrl();
	public String getAbstract();
	public String getFees();
	public String getAccessConstraints();
	public String getFeatureOperationName();
	public String getFeatureOutputFormat();
	
}
