package com.acuity.api.rs.wrappers.peers.scene.elements.impl;

import com.acuity.api.annotations.ClientInvoked;
import com.acuity.api.rs.utils.UIDs;
import com.acuity.api.rs.wrappers.common.SceneElement;
import com.acuity.api.rs.wrappers.common.locations.SceneLocation;
import com.acuity.api.rs.wrappers.common.locations.StrictLocation;
import com.acuity.api.rs.wrappers.common.locations.WorldLocation;
import com.acuity.api.rs.wrappers.peers.rendering.Model;
import com.acuity.api.rs.wrappers.peers.rendering.bounding_boxes.AxisAlignedBoundingBox;
import com.acuity.rs.api.RSAxisAlignedBoundingBox;
import com.acuity.rs.api.RSRenderable;
import com.acuity.rs.api.RSSceneBoundary;
import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;

import java.util.Optional;

/**
 * Created by Zachary Herridge on 6/12/2017.
 */
public class SceneBoundary implements com.acuity.api.rs.wrappers.common.SceneElement {

    private RSSceneBoundary rsSceneBoundary;

    @ClientInvoked
    public SceneBoundary(RSSceneBoundary peer) {
        this.rsSceneBoundary = Preconditions.checkNotNull(peer);
    }

    @NotNull
    public RSSceneBoundary getRsSceneBoundary() {
        return rsSceneBoundary;
    }

    private Optional<RSRenderable> getRenderable(){
        RSRenderable entity = rsSceneBoundary.getEntity();
        return Optional.ofNullable(entity != null ? entity : rsSceneBoundary.getRenderable2());
    }

    @Override
    public Optional<AxisAlignedBoundingBox> getBoundingBox() {
        return getRenderable().map(RSRenderable::getBoundingBox).map(RSAxisAlignedBoundingBox::getWrapper);
    }

    @Override
    public Optional<Model> getModel() {
        return SceneElement.getModel(
                getRenderable().orElse(null),
                getStrictLocation(),
                null);
    }

    public UIDs.UID getUID(){
        return new UIDs.UID(rsSceneBoundary.getUid());
    }

    public int getID(){
        return getUID().getEntityID();
    }

    public StrictLocation getStrictLocation(){
        return new StrictLocation(rsSceneBoundary.getSceneX(), rsSceneBoundary.getSceneY(), rsSceneBoundary.getPlane()); // TODO: 7/1/2017 Rename
    }

    public SceneLocation getSceneLocation(){
        return getStrictLocation().getSceneLocation();
    }

    @Override
    public WorldLocation getWorldLocation() {
        return getSceneLocation().getWorldLocation();
    }
}
