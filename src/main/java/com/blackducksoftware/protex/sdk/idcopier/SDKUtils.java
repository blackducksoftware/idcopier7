package com.blackducksoftware.protex.sdk.idcopier;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.blackducksoftware.sdk.fault.SdkFault;
import com.blackducksoftware.sdk.protex.common.BomRefreshMode;
import com.blackducksoftware.sdk.protex.common.UsageLevel;
import com.blackducksoftware.sdk.protex.license.LicenseInfo;
import com.blackducksoftware.sdk.protex.project.bom.BomComponent;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeApi;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNode;
import com.blackducksoftware.sdk.protex.project.codetree.PartialCodeTree;
import com.blackducksoftware.sdk.protex.project.codetree.identification.CodeMatchExclusion;
import com.blackducksoftware.sdk.protex.project.codetree.identification.CodeMatchIdentification;
import com.blackducksoftware.sdk.protex.project.codetree.identification.CodeMatchIdentificationRequest;
import com.blackducksoftware.sdk.protex.project.codetree.identification.CodeTreeIdentificationInfo;
import com.blackducksoftware.sdk.protex.project.codetree.identification.DeclaredIdentification;
import com.blackducksoftware.sdk.protex.project.codetree.identification.DeclaredIdentificationRequest;
import com.blackducksoftware.sdk.protex.project.codetree.identification.DependencyIdentification;
import com.blackducksoftware.sdk.protex.project.codetree.identification.DependencyIdentificationRequest;
import com.blackducksoftware.sdk.protex.project.codetree.identification.Exclusion;
import com.blackducksoftware.sdk.protex.project.codetree.identification.ExclusionType;
import com.blackducksoftware.sdk.protex.project.codetree.identification.Identification;
import com.blackducksoftware.sdk.protex.project.codetree.identification.IdentificationApi;
import com.blackducksoftware.sdk.protex.project.codetree.identification.IdentificationType;
import com.blackducksoftware.sdk.protex.project.codetree.identification.StringSearchIdentification;
import com.blackducksoftware.sdk.protex.project.codetree.identification.StringSearchIdentificationRequest;

public class SDKUtils {
	
	static Logger 	log 					= 	Logger.getLogger(SDKUtils.class);
	
	protected static final String UNSPECIFIED_LICENSE_STRING = "unspecified";

	public static final String encodeString(String s)
	{
		try {
			s = URLEncoder.encode(s, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			log.warn("Unable to encode: " + s, e);
		}
		return s;
	}
	
	/**
	 * This does not actually decode, but escapes HTML characters that accumulate during the encoding process
	 * @param s
	 * @return
	 */
	public static final String decodeString(String s)
	{
		try {
			s = StringEscapeUtils.unescapeHtml(s);
		} catch (Exception e) {
			log.warn("Unable to escape html: " + s, e);
		}
		return s;
	}
	
	protected static PartialCodeTree getCodeTree(CodeTreeApi codeTreeApi,
			String projectId, String path, int level) {
		PartialCodeTree codeTree = null;
		try {
			codeTree = codeTreeApi.getCodeTree(projectId, path, level,
					Boolean.TRUE);

		} catch (SdkFault e) {
			System.err.println("getCodeTree failed: " + e.getMessage());
			System.exit(-1);
		}
		if (codeTree == null) {
			System.err.println("no getCodeTree returned");
			System.exit(-1);
		}
		return codeTree;
	}

	protected static PartialCodeTree createPartialCodeTreeForNode(
			String parentPath, CodeTreeNode node) {
		PartialCodeTree partialCodeTree = new PartialCodeTree();

		partialCodeTree.setParentPath(parentPath);
		partialCodeTree.getNodes().add(node);

		return partialCodeTree;
	}

	protected static String generateHashKey(BomComponent component) {
		String bomCompId = component.getComponentId();
		String bomVersionId = component.getVersionId();
		String bomLicenseId = (component.getLicenseInfo() != null) ? component
				.getLicenseInfo().getLicenseId() : UNSPECIFIED_LICENSE_STRING;

		return bomCompId + "|" + bomVersionId + "|" + bomLicenseId;
	}

	protected static String generateHashKey(Identification id) {
		String compId = id.getIdentifiedComponentId();
		String versionId = id.getIdentifiedVersionId();

		String licenseId = (id.getIdentifiedLicenseInfo() != null) ? id
				.getIdentifiedLicenseInfo().getLicenseId()
				: UNSPECIFIED_LICENSE_STRING;
		return compId + "|" + versionId + "|" + licenseId;
	}

	protected static boolean compareToIdentification(Identification id,
			String componentId, String versionId, String licenseId) {
		return compareToIdentification(id, componentId, versionId, licenseId,
				(id.getIdentifiedUsageLevel() != null) ? id
						.getIdentifiedUsageLevel().value() : null);
	}

	protected static boolean compareToIdentification(Identification id,
			String componentId, String versionId, String licenseId,
			String usageLevel)

	{
		if (id.getIdentifiedComponentId().equals(componentId))

			;// System.out.println("<<Comp ID Match with " + componentId +
				// ">>");
		else
			return false;

		if ((id.getIdentifiedVersionId() == null && versionId == null)
				|| (id.getIdentifiedVersionId() != null && id
						.getIdentifiedVersionId().equals(versionId)))
			;// System.out.println("<<Version Match with " + versionId + ">>");
		else
			return false;

		LicenseInfo lic = id.getIdentifiedLicenseInfo();

		if ((lic == null && licenseId == null) || lic != null
				&& lic.getLicenseId().equals(licenseId))

			;// System.out.println("<<License Match with " + licenseId + ">>");
		else
			return false;

		UsageLevel ul = id.getIdentifiedUsageLevel();

		if (ul == null)
			;// System.out.println("<<Usage Match with NULL>>");
		else if (ul != null && ul.name().equals(usageLevel))
			;// System.out.println("<<Usage Match with " + usageLevel + ">>");
		else
			return false;

		return true;

	}

	protected static void printComponentInfo(BomComponent component) {
		System.out.println();
		String bomCompId = component.getComponentId();
		String bomVersionId = component.getVersionId();
		String bomLicenseId = (component.getLicenseInfo() != null) ? component
				.getLicenseInfo().getLicenseId() : null;

		System.out.println("#component.getComponentId()=" + bomCompId);
		System.out.println("#component.getVersionId()=" + bomVersionId);
		System.out.println("#component.getBomVersionName()="
				+ component.getBomVersionName());
		System.out.println("#component.getLicenseInfo().getLicenseId()="
				+ bomLicenseId);

		List<UsageLevel> usages = component.getUsageLevels();
		for (UsageLevel ul : usages) {
			System.out.println("#ul.value()=" + ul.value());
		}

		System.out.println("#component.getFileCountSingleIdentification()="
				+ component.getFileCountSingleIdentification());
		System.out.println("#component.getFileCountIdentified()="
				+ component.getFileCountIdentified());
		System.out.println("#component.getFileCountStringSearches()="
				+ component.getFileCountStringSearches());
		System.out.println("#component.getFileCountDependencies()="
				+ component.getFileCountDependencies());
		System.out.println("#component.getFileCountMultipleIdentifications()="
				+ component.getFileCountMultipleIdentifications());
		System.out.println("#component.isIsDeclaredComponentFlag()="
				+ component.isDeclaredComponentFlag());
	}

	protected static void printIdentificationInfo(Identification id) {
		System.out.println();
		System.out.println("#id.getIdentifiedComponentId()="
				+ id.getIdentifiedComponentId());
		System.out.println("#id.getIdentifiedVersionId()="
				+ id.getIdentifiedVersionId());

		String licenseId = (id.getIdentifiedLicenseInfo() != null) ? id
				.getIdentifiedLicenseInfo().getLicenseId() : "null";

		System.out.println("#id.getIdentifiedLicenseInfo().getLicenseId()="
				+ licenseId);

		System.out.println("#id.getIdentifiedUsageLevel().value()="
				+ ((id.getIdentifiedUsageLevel() != null) ? id
						.getIdentifiedUsageLevel().value() : "null"));
		System.out.println("#id.getType().value()=" + id.getType().value());
		
		if(id.getType().equals(IdentificationType.CODE_MATCH))
		{
			CodeMatchIdentification cmid = (CodeMatchIdentification)id;
			System.out.println("#cmid.getPath()=" + cmid.getPath());
			System.out.println("#cmid.getDiscoveredComponentId()=" +cmid.getDiscoveredComponentId());
			System.out.println("#cmid.getDiscoveredVersionId()=" + cmid.getDiscoveredVersionId());
		}
		else
		{
			DeclaredIdentification did = (DeclaredIdentification)id;
			System.out.println("#did.getAppliedToPath()=" + did.getAppliedToPath());
		}

	}

	protected static void printExclusionInfo(List<Exclusion> exs) {
		for (Exclusion ex : exs)

			if (ex.getType().equals(ExclusionType.CODE_MATCH)) {
				CodeMatchExclusion excm = (CodeMatchExclusion) ex;
				System.out.println("#excm.getDiscoveredComponentId()="
						+ excm.getDiscoveredComponentId());
				System.out.println("#excm.getPath()=" + excm.getPath());

			}

	}

	protected static String getParentPath(String path) {

		if (path == "/")
			return null;

		int last = path.lastIndexOf("/");
		if (last == 0)
			return "/";
		else
			return path.substring(0, last);
	}

	protected static CodeTreeIdentificationInfo getParentIdTree(
			IdentificationApi identificationApi, CodeTreeApi codeTreeApi,
			Map<String, CodeTreeIdentificationInfo> idTreeCache,
			String projectId, String parentPath) {

		// System.out.println("Generating Identification Code Tree for path: " +
		// parentPath);

		CodeTreeIdentificationInfo parentIdTree = null;

		if (idTreeCache.get(parentPath) != null) {
			parentIdTree = idTreeCache.get(parentPath);
			// System.out.println("Obtained From Cache");
		} else

			try {
				parentIdTree = identificationApi.getAppliedIdentifications(
						projectId,
						SDKUtils.getCodeTree(codeTreeApi, projectId,
								parentPath, 0)).get(0);

				idTreeCache.put(parentPath, parentIdTree);
				// System.out.println("Obtained From Web Service Call");

			} catch (SdkFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return parentIdTree;
	}

	protected static void createCodeMatchIdRequest(String targetProjectId,
			IdentificationApi identificationApi, Identification id,
			String nodePath, BomRefreshMode mode) {
		CodeMatchIdentification cmid = (CodeMatchIdentification) id;
		CodeMatchIdentificationRequest cmidReq = new CodeMatchIdentificationRequest();
		cmidReq.setCodeMatchIdentificationDirective(cmid
				.getCodeMatchIdentificationDirective());
		cmidReq.setDiscoveredComponentId(cmid.getDiscoveredComponentId());
		cmidReq.setDiscoveredVersionId(cmid.getDiscoveredVersionId());
		cmidReq.setIdentifiedComponentId(cmid.getIdentifiedComponentId());
		cmidReq.setIdentifiedLicenseInfo(cmid.getIdentifiedLicenseInfo());
		cmidReq.setIdentifiedUsageLevel(cmid.getIdentifiedUsageLevel());
		cmidReq.setIdentifiedVersionId(cmid.getIdentifiedVersionId());
		System.out.println("Adding Code Match Identification for " + nodePath);
		try {
			identificationApi.addCodeMatchIdentification(targetProjectId,
					nodePath, cmidReq, mode);
		} catch (SdkFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void createDeclarationIdRequest(String targetProjectId,
			IdentificationApi identificationApi, Identification id,
			String nodePath,  BomRefreshMode mode) {
		DeclaredIdentification did = (DeclaredIdentification) id;

		DeclaredIdentificationRequest didReq = new DeclaredIdentificationRequest();
		didReq.setIdentifiedComponentId(did.getIdentifiedComponentId());
		didReq.setIdentifiedLicenseInfo(did.getIdentifiedLicenseInfo());
		didReq.setIdentifiedUsageLevel(did.getIdentifiedUsageLevel());
		didReq.setIdentifiedVersionId(did.getIdentifiedVersionId());
		didReq.setPath(nodePath);

		System.out.println("Adding Declared Identification for " + nodePath);
		try {
			identificationApi.addDeclaredIdentification(targetProjectId,
					nodePath, didReq, mode);
		} catch (SdkFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void createStringSearchIdRequest(String targetProjectId,
			IdentificationApi identificationApi, Identification id,
			String nodePath,  BomRefreshMode mode) {
		StringSearchIdentification ssid = (StringSearchIdentification) id;
		StringSearchIdentificationRequest ssidReq = new StringSearchIdentificationRequest();
		ssidReq.setIdentifiedComponentId(ssid.getIdentifiedComponentId());
		ssidReq.setPath(nodePath);
		ssidReq.setIdentifiedLicenseInfo(ssid.getIdentifiedLicenseInfo());
		ssidReq.setIdentifiedUsageLevel(ssid.getIdentifiedUsageLevel());
		ssidReq.setIdentifiedVersionId(ssid.getIdentifiedVersionId());
		ssidReq.setFolderLevelIdentification(ssid
				.isFolderLevelIdentification());
		ssidReq.setStringSearchId(ssid.getStringSearchId());
		ssidReq.getMatchLocations().addAll(ssid.getMatchLocations());
		System.out.println("Adding String Search Identification for "
				+ nodePath);
		try {
			identificationApi.addStringSearchIdentification(targetProjectId,
					nodePath, ssidReq, mode);
		} catch (SdkFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void createDependencyIdRequest(String targetProjectId,
			IdentificationApi identificationApi, Identification id,
			String nodePath,  BomRefreshMode mode) {
		DependencyIdentification did = (DependencyIdentification) id;
		DependencyIdentificationRequest didReq = new DependencyIdentificationRequest();
		didReq.setIdentifiedComponentId(did.getIdentifiedComponentId());
		didReq.setPath(nodePath);
		didReq.setIdentifiedLicenseInfo(did.getIdentifiedLicenseInfo());
		didReq.setIdentifiedUsageLevel(did.getIdentifiedUsageLevel());
		didReq.setIdentifiedVersionId(did.getIdentifiedVersionId());
		didReq.setDependencyType(did.getDependencyType());
		didReq.setDependencyId(did.getDependencyId());

		System.out.println("Adding Dependency Identification for " + nodePath);
		try {
			identificationApi.addDependencyIdentification(targetProjectId,
					nodePath, didReq, mode);
		} catch (SdkFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static CodeTreeIdentificationInfo getAppliedIdentificationInfo(
			IdentificationApi identificationApi, String projectId,
			String parentPath, CodeTreeNode node) {
		CodeTreeIdentificationInfo nodeIdentification = null;

		try {
			nodeIdentification = identificationApi.getAppliedIdentifications(
					projectId,
					SDKUtils.createPartialCodeTreeForNode(parentPath, node))
					.get(0);
		} catch (SdkFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodeIdentification;
	}

	private static CodeTreeIdentificationInfo getEffectiveIdentificationInfo(
			IdentificationApi identificationApi, String projectId,
			String parentPath, CodeTreeNode node) {
		CodeTreeIdentificationInfo nodeIdentification = null;

		try {
			nodeIdentification = identificationApi.getEffectiveIdentifications(
					projectId,
					SDKUtils.createPartialCodeTreeForNode(parentPath, node))
					.get(0);
		} catch (SdkFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodeIdentification;
	}
	
	
	protected static List<Identification> getAppliedIdsForNode(
			IdentificationApi identificationApi, String projectId,
			String parentPath, CodeTreeNode node) {

		return getAppliedIdentificationInfo(identificationApi, projectId, parentPath,
				node).getIdentifications();

	}

	protected static List<Identification> getEffectiveIdsForNode(
			IdentificationApi identificationApi, String projectId,
			String parentPath, CodeTreeNode node) {

		return getEffectiveIdentificationInfo(identificationApi, projectId, parentPath,
				node).getIdentifications();

	}
	
	protected static List<Exclusion> getExclusionsForNode(IdentificationApi identificationApi,
			String projectId, String parentPath, CodeTreeNode node) {
		return getAppliedIdentificationInfo(identificationApi, projectId, parentPath,
				node).getExclusions();

	}

}
