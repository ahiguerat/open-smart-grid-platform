package com.smartgrid.ikusi.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.apache.tools.tar.TarOutputStream;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.Firmware;

import com.google.common.base.Enums;
import com.smartgrid.ikusi.enums.Module;

public class FileServices {
	
	HashMap<Module,String> modules; 
	ByteArrayOutputStream output;
	private TarOutputStream out;

	private String getVersion(ByteArrayOutputStream stream) throws IOException {
		String lines[] = stream.toString("UTF-8").split("\\r?\\n");
		String version = lines[0].replace("=", ".");
		return version;

	}

	private String renameFile(String name, String version) {
		String parts[] = name.split("\\.");
		String extension = "";
		String finalName = "";
		String firstName = parts[0];
		String versus = "";
		int length = parts.length - 1;
		if (length > 0) {
			extension = parts[length];
			parts[length] = version;
			//finalName = String.join(".", parts) + "." + extension;
			finalName = String.format("%s.%s.%s", firstName, version, extension);
			versus = String.format("%s.%s", firstName, version);
		} else {
			finalName += "." + version;
			versus = finalName;
		}
						
		if( Enums.getIfPresent(Module.class, firstName.toUpperCase()).isPresent()) {
			this.modules.put(Module.valueOf(firstName.toUpperCase()), versus);			
		}
		//System.out.println(finalName);
		return finalName;
	}

	private void createRoot() throws IOException {
		this.output = new ByteArrayOutputStream();
		this.out = new TarOutputStream(new GZIPOutputStream(output));
		this.modules = new HashMap<Module,String>();
	}

	@SuppressWarnings("unused")
	public void readCompressFile(Firmware firmware) {
		String versionFirmware = "";
		String fileVersion = "VER_CCP";

		try {
			if (firmware.getFile() != null && firmware.getFile().length > 0) {
				this.createRoot();
				// get the zip file content
				TarInputStream zis = new TarInputStream(
						new GZIPInputStream(new ByteArrayInputStream(firmware.getFile())));
				TarEntry ze = zis.getNextEntry();

				while (ze != null) {

					if (!ze.isDirectory()) {

						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						byte[] buffer = new byte[1024];
						String fileName = ze.getName();

						// Cargamos los archivos en el buffer
						for (int readNum; (readNum = zis.read(buffer)) != -1;) {
							stream.write(buffer, 0, readNum);
						}

						// Verifica si el nombre del archivo es VER_CCP
						if (fileName.contains(fileVersion)) {
							versionFirmware = this.getVersion(stream);
							String rename = this.renameFile(firmware.getFilename(), versionFirmware);							
							firmware.setFilename(rename);

						} else {

							String parts[] = fileName.split("\\.");
							if (parts.length > 0 && parts[parts.length - 1].equals("tgz")) {

								TarInputStream subFile = new TarInputStream(
										new GZIPInputStream(new ByteArrayInputStream(stream.toByteArray())));
								TarEntry sb = subFile.getNextEntry();

								while (sb != null) {
									String subfileName = sb.getName();
									if (subfileName.contains(fileVersion)) {

										ByteArrayOutputStream streamSF = new ByteArrayOutputStream();
										byte[] bufferSF = new byte[1024];

										// Cargamos los archivos en el buffer
										for (int readNum; (readNum = subFile.read(bufferSF)) != -1;) {
											streamSF.write(bufferSF, 0, readNum);
										}

										String versionSF = this.getVersion(streamSF);
										fileName = this.renameFile(fileName, versionSF);
										sb.setName(fileName);

									}

									sb = subFile.getNextEntry();
								}
								subFile.close();
							}
						}

						TarEntry file = new TarEntry(fileName);
						file.setSize(stream.toByteArray().length);
						out.putNextEntry(file);
						out.write(stream.toByteArray());
						out.closeEntry();
						stream.close();
					}

					ze = zis.getNextEntry();
				}

				zis.close();

				out.close();				
				//firmware.setFile(output.toByteArray());
				output.close();
				
			
				for(Map.Entry<Module, String> entry : this.modules.entrySet()) {
					Module key = entry.getKey();
				    String value = entry.getValue();
				    
				    switch(key) {
				    	
				    	case XMLLINT:				    		
				    		firmware.getFirmwareModuleData().setModuleVersionXmllint(value);
				    		//firmware.getFirmwareModuleData().setModuleVersionFunc(value);
				    		break;
				    	case XML2CCP:
				    		firmware.getFirmwareModuleData().setModuleVersionXml2Ccp(value);
				    		//firmware.getFirmwareModuleData().setModuleVersionMa(value);
				    		break;
				    	case LIBMMSLITE:
				    		firmware.getFirmwareModuleData().setModuleVersionLibmmslite(value);;
				    		//firmware.getFirmwareModuleData().setModuleVersionSec(value);
				    		break;
				    	case EKORCCP:
				    		firmware.getFirmwareModuleData().setModuleVersionEkorccp(value);
				    		//firmware.getFirmwareModuleData().setModuleVersionMbus(value);
				    		break;
				    	case DIMXCCP:
				    		firmware.getFirmwareModuleData().setModuleVersionDimxccp(value);
				    		break;
				    	case RTUSCHEMAS:
				    		firmware.getFirmwareModuleData().setModuleVersionRtuschemas(value);
				    		break;
				    	case LOCALTIME:
				    		firmware.getFirmwareModuleData().setModuleVersionLocaltime(value);
				    		break;
				    	case LIBXSDSET:
				    		firmware.getFirmwareModuleData().setModuleVersionLibxsdset(value);
				    		break;
				    	case FREEDISK:
				    		firmware.getFirmwareModuleData().setModuleVersionFreedisk(value);
				    		break;
				    	case EKORRTUWS:
				    		firmware.getFirmwareModuleData().setModuleVersionEkorrtuws(value);
				    		break;
				    	case EKORWEB:
				    		firmware.getFirmwareModuleData().setModuleVersionEkorWeb(value);
				    		break;
				    	case CCPC:
				    		firmware.getFirmwareModuleData().setModuleVersionCcpC(value);
				    		break;
				    	case DARMCCP:
				    		firmware.getFirmwareModuleData().setModuleVersionDarmccp(value);
				    		break;
				    	case EXPECT:
				    		firmware.getFirmwareModuleData().setModuleVersionExpect(value);
				    		break;
				    	case OPENSSH:
				    		firmware.getFirmwareModuleData().setModuleVersionOpenssh(value);
				    		break;
				    	case OPENSSL:
				    		firmware.getFirmwareModuleData().setModuleVersionOpenssl(value);
				    		break;
				    	case PROFTPD:
				    		firmware.getFirmwareModuleData().setModuleVersionProftpd(value);
				    		break;
				    	case TCPDUMP:
				    		firmware.getFirmwareModuleData().setModuleVersionTcpdump(value);
				    		break;
				    	default: break;				    
				    }


				}
				
			}

		} catch (IOException e) {
			System.out.println(e);
		}
	}

}
