package io.gaiapipeline;

import io.gaiapipeline.javasdk.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class Pipeline
{
    private static final Logger LOGGER = Logger.getLogger(Pipeline.class.getName());

    private static Handler CreateUser = (gaiaArgs) -> {
        LOGGER.info("CreateUser has been started!");
        Thread.sleep(5000);
        LOGGER.info("CreateUser has been finished!");
    };

    private static Handler MigrateDB = (gaiaArgs) -> {
        LOGGER.info("MigrateDB has been started!");
        Thread.sleep(5000);
        LOGGER.info("MigrateDB has been finished!");
    };

    private static Handler CreateNamespace = (gaiaArgs) -> {
        LOGGER.info("CreateNamespace has been started!");
        Thread.sleep(5000);
        LOGGER.info("CreateNamespace has been finished!");
    };

    private static Handler CreateDeployment = (gaiaArgs) -> {
        LOGGER.info("CreateDeployment has been started!");
        Thread.sleep(5000);
        LOGGER.info("CreateDeployment has been finished!");
    };

    private static Handler CreateService = (gaiaArgs) -> {
        LOGGER.info("CreateService has been started!");
        Thread.sleep(5000);
        LOGGER.info("CreateService has been finished!");
    };

    private static Handler CreateIngress = (gaiaArgs) -> {
        LOGGER.info("CreateIngress has been started!");
        Thread.sleep(5000);
        LOGGER.info("CreateIngress has been finished!");
    };

    private static Handler Cleanup = (gaiaArgs) -> {
        LOGGER.info("Cleanup has been started!");
        Thread.sleep(5000);
        LOGGER.info("Cleanup has been finished!");
    };

    public static void main( String[] args )
    {
        PipelineJob createuser = new PipelineJob();
        createuser.setTitle("Create DB User");
        createuser.setDescription("Creates a database user with least privileged permissions.");
        createuser.setHandler(CreateUser);

        PipelineJob migratedb = new PipelineJob();
        migratedb.setTitle("DB Migration");
        migratedb.setDescription("Imports newest test data dump and migrates to newest version.");
        migratedb.setHandler(MigrateDB);
        migratedb.setDependsOn(new ArrayList<>(Arrays.asList("Create DB User")));

        PipelineJob createnamespace = new PipelineJob();
        createnamespace.setTitle("Create K8S Namespace");
        createnamespace.setDescription("Creates a new Kubernetes namespace for the new test environment.");
        createnamespace.setHandler(CreateNamespace);
        createnamespace.setDependsOn(new ArrayList<>(Arrays.asList("DB Migration")));

        PipelineJob createdeployment = new PipelineJob();
        createdeployment.setTitle("Create K8S Deployment");
        createdeployment.setDescription("Creates a new Kubernetes deployment for the new test environment.");
        createdeployment.setHandler(CreateDeployment);
        createdeployment.setDependsOn(new ArrayList<>(Arrays.asList("Create K8S Namespace")));

        PipelineJob createservice = new PipelineJob();
        createservice.setTitle("Create K8S Service");
        createservice.setDescription("Creates a new Kubernetes service for the new test environment.");
        createservice.setHandler(CreateService);
        createservice.setDependsOn(new ArrayList<>(Arrays.asList("Create K8S Namespace")));

        PipelineJob createingress = new PipelineJob();
        createingress.setTitle("Create K8S Ingress");
        createingress.setDescription("Creates a new Kubernetes ingress for the new test environment.");
        createingress.setHandler(CreateIngress);
        createingress.setDependsOn(new ArrayList<>(Arrays.asList("Create K8S Namespace")));

        PipelineJob cleanup = new PipelineJob();
        cleanup.setTitle("Clean up");
        cleanup.setDescription("Removes all temporary files.");
        cleanup.setHandler(Cleanup);
        cleanup.setDependsOn(new ArrayList<>(Arrays.asList("Create K8S Deployment", "Create K8S Service", "Create K8S Ingress")));

        Javasdk sdk = new Javasdk();
        try {
            sdk.Serve(new ArrayList<>(Arrays.asList(createuser, migratedb, createnamespace, createdeployment, createservice, createingress, cleanup)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
