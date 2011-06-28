package info.jasonfried.jira.plugin.workflow.validator;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.google.common.collect.ImmutableMap;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Project Key Validator Factory, allow user to select which customfield they wish to validate as a Project Key.
 */
public class ProjectKeyValidatorFactory
        extends AbstractWorkflowPluginFactory
        implements WorkflowPluginValidatorFactory {

    public static final String SELECTED_KEY_FIELD = "selected-key-field";
    private static final String KEY_FIELD = "key-field";

    private CustomFieldManager customFieldManager;
    public ProjectKeyValidatorFactory(CustomFieldManager customFieldManager){
        this.customFieldManager = customFieldManager;
    }

    @Override
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        List<CustomField> customfields = this.customFieldManager.getCustomFieldObjects();
        HashMap<String,String> fieldselect = new HashMap<String, String>();
        Iterator<CustomField> iterator = customfields.iterator();
        while ( iterator.hasNext() ) {
            CustomField field = iterator.next();
            StringBuilder sb = new StringBuilder();
            //Build Name + ID for use as a select value.
            sb.append(field.getName()).append('(').append(field.getId()).append(')');
            fieldselect.put(field.getId(), sb.toString());
        }
        velocityParams.put(KEY_FIELD, fieldselect);
    }

    @Override
    protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);
        velocityParams.put(SELECTED_KEY_FIELD,
                (String) ((ValidatorDescriptor) descriptor).getArgs().get(SELECTED_KEY_FIELD));
    }

    @Override
    protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        velocityParams.put(SELECTED_KEY_FIELD,
                (String) ((ValidatorDescriptor) descriptor).getArgs().get(SELECTED_KEY_FIELD));
    }

    @Override
    public Map<String, Object> getDescriptorParams(Map<String, Object> validatorParams) {
        String value = extractSingleParam(validatorParams, SELECTED_KEY_FIELD);
        return ImmutableMap.<String,Object>of(SELECTED_KEY_FIELD, value);
    }
}
