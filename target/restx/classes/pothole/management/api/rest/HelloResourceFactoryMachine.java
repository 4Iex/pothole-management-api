package pothole.management.api.rest;

import com.google.common.collect.ImmutableSet;
import restx.factory.*;
import pothole.management.api.rest.HelloResource;

@Machine
public class HelloResourceFactoryMachine extends SingleNameFactoryMachine<pothole.management.api.rest.HelloResource> {
    public static final Name<pothole.management.api.rest.HelloResource> NAME = Name.of(pothole.management.api.rest.HelloResource.class, "HelloResource");

    public HelloResourceFactoryMachine() {
        super(0, new StdMachineEngine<pothole.management.api.rest.HelloResource>(NAME, 0, BoundlessComponentBox.FACTORY) {


            @Override
            public BillOfMaterials getBillOfMaterial() {
                return new BillOfMaterials(ImmutableSet.<Factory.Query<?>>of(

                ));
            }

            @Override
            protected pothole.management.api.rest.HelloResource doNewComponent(SatisfiedBOM satisfiedBOM) {
                return new HelloResource(

                );
            }
        });
    }

}
