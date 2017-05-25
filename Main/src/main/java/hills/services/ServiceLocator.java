package hills.services;

import hills.services.ModelDataService.CubeModel;
import hills.services.ModelDataService.IModelService;
import hills.services.camera.ICameraDataService;
import hills.services.camera.ICameraUpdateService;
import hills.services.camera.CameraService;
import hills.services.debug.DebugService;
import hills.services.display.DisplayService;
import hills.services.display.DisplayServiceI;
import hills.services.files.FileService;
import hills.services.files.IPictureFileService;
import hills.services.generation.GenerationMediator;
import hills.services.generation.IGenerationMediator;
import hills.services.terrain.ITerrainHeightService;
import hills.services.terrain.ITerrainRenderDataService;
import hills.services.terrain.TerrainService;
import hills.services.terrain.ITerrainTreeService;

import static hills.services.ModelDataService.ModelFactory.getModelServiceInstance;
import static hills.services.camera.CameraFactory.getCameraServiceInstance;
import static hills.services.display.DisplayFactory.getDisplayServiceInstance;
import static hills.services.files.FileFactory.getFileServiceInstance;
import static hills.services.terrain.TerrainFactory.getTerrainServiceInstance;

public enum ServiceLocator {
	INSTANCE;
	
	private DisplayService displayService;
	private DebugService debugService;
	private TerrainService terrainService;
	private FileService fileService;
	private IGenerationMediator generationService;
	private IModelService modelService;
	
	private ServiceLocator(){	
	}
	
	public ITerrainHeightService getTerrainHeightService(){
		return getTerrainServiceInstance(false);
	}
	
	public ITerrainRenderDataService getTerrianRenderDataService(){
		return getTerrainServiceInstance(false);
	}

	public ITerrainHeightService getTerrainHeightTestService(){
		return getTerrainServiceInstance(true);
	}
	
	public ITerrainTreeService getTerrainTreeService(){
		return getTerrainServiceInstance(false);
	}
	
	public ICameraUpdateService getCameraUpdateService(){
		return getCameraServiceInstance();
	}

	public ICameraDataService getCameraDataService(){
		return getCameraServiceInstance();
	}

	public IGenerationMediator getGenerationService(){
		return getGemerationServiceInstance();
	}

	private IGenerationMediator getGemerationServiceInstance() {
		if(generationService == null){
			generationService = new GenerationMediator();
		}
		return generationService;
	}

	public DisplayServiceI getDisplayService(){
		return getDisplayServiceInstance();
	}

	public IModelService getModelService(){
		return getModelServiceInstance();
	}

	public IPictureFileService getFileService() { return getFileServiceInstance(); }
}
