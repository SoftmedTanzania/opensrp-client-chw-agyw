package org.smartregister.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.agyw.contract.TestRegisterFragmentContract;
import org.smartregister.chw.agyw.presenter.BaseTestRegisterFragmentPresenter;
import org.smartregister.chw.agyw.util.Constants;
import org.smartregister.chw.agyw.util.DBConstants;
import org.smartregister.configurableviews.model.View;

import java.util.Set;
import java.util.TreeSet;

public class BaseTestRegisterFragmentPresenterTest {
    @Mock
    protected TestRegisterFragmentContract.View view;

    @Mock
    protected TestRegisterFragmentContract.Model model;

    private BaseTestRegisterFragmentPresenter baseTestRegisterFragmentPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        baseTestRegisterFragmentPresenter = new BaseTestRegisterFragmentPresenter(view, model, "");
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseTestRegisterFragmentPresenter);
    }

    @Test
    public void getMainCondition() {
        Assert.assertEquals("", baseTestRegisterFragmentPresenter.getMainCondition());
    }

    @Test
    public void getDueFilterCondition() {
        Assert.assertEquals(" (cast( julianday(STRFTIME('%Y-%m-%d', datetime('now'))) -  julianday(IFNULL(SUBSTR(agyw_test_date,7,4)|| '-' || SUBSTR(agyw_test_date,4,2) || '-' || SUBSTR(agyw_test_date,1,2),'')) as integer) between 7 and 14) ", baseTestRegisterFragmentPresenter.getDueFilterCondition());
    }

    @Test
    public void getDefaultSortQuery() {
        Assert.assertEquals(Constants.TABLES.AGYW_CONFIRMATION + "." + DBConstants.KEY.LAST_INTERACTED_WITH + " DESC ", baseTestRegisterFragmentPresenter.getDefaultSortQuery());
    }

    @Test
    public void getMainTable() {
        Assert.assertEquals(Constants.TABLES.AGYW_CONFIRMATION, baseTestRegisterFragmentPresenter.getMainTable());
    }

    @Test
    public void initializeQueries() {
        Set<View> visibleColumns = new TreeSet<>();
        baseTestRegisterFragmentPresenter.initializeQueries(null);
        Mockito.doNothing().when(view).initializeQueryParams("ec_agyw_confirmation", null, null);
        Mockito.verify(view).initializeQueryParams("ec_agyw_confirmation", null, null);
        Mockito.verify(view).initializeAdapter(visibleColumns);
        Mockito.verify(view).countExecute();
        Mockito.verify(view).filterandSortInInitializeQueries();
    }

}