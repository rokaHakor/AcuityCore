package com.acuity.api.rs.wrappers.common;

import com.acuity.api.AcuityInstance;
import com.acuity.api.rs.interfaces.Interactive;
import com.acuity.api.rs.interfaces.Locatable;
import com.acuity.api.rs.interfaces.Nameable;
import com.acuity.api.rs.utils.Varps;
import com.acuity.api.rs.wrappers.common.locations.StrictLocation;
import com.acuity.api.rs.wrappers.common.locations.screen.ScreenLocationShape;
import com.acuity.api.rs.wrappers.peers.composite.SceneElementComposite;
import com.acuity.api.rs.wrappers.peers.engine.Varpbit;
import com.acuity.api.rs.wrappers.peers.rendering.Model;
import com.acuity.api.rs.wrappers.peers.rendering.bounding_boxes.AxisAlignedBoundingBox;
import com.acuity.rs.api.RSModel;
import com.acuity.rs.api.RSRenderable;
import com.acuity.rs.api.RSSceneElementComposite;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by Zach on 6/24/2017.
 */
public interface SceneElement extends Locatable, Nameable, Interactive {

    static Optional<Model> getModel(RSRenderable rsRenderable, StrictLocation location, Integer orientation) {
        if (rsRenderable == null) return Optional.empty();

        Model lastModel;
        if (rsRenderable instanceof RSModel) lastModel = new Model((RSModel) rsRenderable);
        else lastModel = rsRenderable.getCachedModel();

        return Optional.ofNullable(lastModel)
                .map(model -> model.place(location.getX() , location.getY()))
                .map(model -> {
                    if (orientation != null) model.rotateTo(orientation);
                    return model;
                });
    }

    @SuppressWarnings("unchecked")
    default List<String> getActions(){
        return getComposite().map(SceneElementComposite::getActions).map(Arrays::asList).orElse(Collections.EMPTY_LIST);
    }

    @Override
    default Supplier<Optional<ScreenLocationShape>> getScreenTargetSupplier(){
        if (!AcuityInstance.getSettings().isModelInteractionsEnabled()){
            return () -> getBoundingBox().map(AxisAlignedBoundingBox::getScreenTargetSupplier).map(Supplier::get).orElseGet(Optional::empty);
        }
        return () -> getModel().map(Model::getScreenTargetSupplier).map(Supplier::get).orElseGet(Optional::empty);
    }

    Optional<AxisAlignedBoundingBox> getBoundingBox();

    Optional<Model> getModel();

    int getID();

    default String getName(){
        return getComposite().map(SceneElementComposite::getName).orElse(null);
    }

    default Optional<SceneElementComposite> getComposite(){
        RSSceneElementComposite rsSceneElementComposite = AcuityInstance.getClient().getRsClient().invokeGetObjectDefinition(getID());

        if (rsSceneElementComposite != null){
            int[] transformIDs = rsSceneElementComposite.getTransformIDs();

            if (transformIDs != null){
                int varpbitIndex = rsSceneElementComposite.getVarpbitIndex();
                int varpIndex = rsSceneElementComposite.getVarpIndex();

                int transformedIndex = -1;
                if (varpbitIndex != -1){
                    transformedIndex = Varps.getVarpBit(varpbitIndex).map(Varpbit::getValue).orElse(-1);
                }
                else if (varpIndex != -1){
                    transformedIndex = Varps.get(varpIndex, -1);
                }

                if (transformedIndex != -1) {
                    rsSceneElementComposite = AcuityInstance.getClient().getRsClient().invokeGetObjectDefinition(transformIDs[transformedIndex]);
                }
            }
        }

        return Optional.ofNullable(rsSceneElementComposite).map(RSSceneElementComposite::getWrapper);
    }
}
