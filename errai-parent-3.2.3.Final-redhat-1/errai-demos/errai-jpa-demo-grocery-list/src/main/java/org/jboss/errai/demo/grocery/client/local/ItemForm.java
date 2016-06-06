/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.errai.demo.grocery.client.local;

import static com.google.gwt.dom.client.Style.Unit.PX;

import java.util.Date;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.validation.ConstraintViolation;

import org.jboss.errai.demo.grocery.client.shared.Department;
import org.jboss.errai.demo.grocery.client.shared.Item;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.Window;

/**
 * A form for editing the properties of a new or existing Item object.
 * 
 * @author Jonathan Fuerth <jfuerth@gmail.com>
 */
@Dependent
@Templated("#form")
public class ItemForm extends Form {

  
  
  /**
   * Event handler functions for user events generated by clicking on the form buttons
   * @param event
   */
  // TODO (after ERRAI-366): make this method package-private
  @EventHandler("saveButton")
  public void onSaveButtonClicked(ClickEvent event) {
    if(!isValidName())
      return;
   
    String departmentText = department.getText();
    int count = 0;
   while (departmentText == null) {
     departmentText = department.getText();
     System.out.println(count);
     count++;
     
   }
      
    Department resolvedDepartment = Department.resolve(em, department.getText());
    Item item = itemBinder.getModel();
    //item.setName(name.getText());
    item.setDepartment(resolvedDepartment);
    //item.setComment(comment.getText());
    item.setAddedBy(user);
    item.setAddedOn(new Date());

    final Set<ConstraintViolation<Item>> violations = validator.validate(item);
    if (violations.size() > 0) {
      ConstraintViolation<Item> violation = violations.iterator().next();
      overallErrorMessage.setText(violation.getPropertyPath() + " " + violation.getMessage());
      overallErrorMessage.setVisible(true);
      return;
    }
    
    em.persist(item);
    
    //groceryList.getItems().add(item);
    //em.merge(groceryList);
    
    em.flush();

    clearButton.click();
    hideOtherFields();

    if (afterSaveAction != null) {
      afterSaveAction.run();
    }
  }
  
  @EventHandler("clearButton")
  public void onClearButtonClicked(ClickEvent clearFormEvent) {
    name.setText("");
    department.setText("");
    comment.setText("");
    
    name.getValueBox().addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        if ("0px".equals(otherFields.getStyle().getHeight())) {
          new Animation() {
            @Override
            protected void onUpdate(double progress) {
              otherFields.getStyle().setHeight(Window.getClientWidth() > 768 ? 215 : 145 * progress, PX);
            }
          }.run(1000);
        }
      }
    });
    grabKeyboardFocus();
  }
  

  
  /**
   * Private helper functions
   */

  public void setAfterSaveAction(Runnable afterSaveAction) {
    this.afterSaveAction = afterSaveAction;
  }
  
 }
