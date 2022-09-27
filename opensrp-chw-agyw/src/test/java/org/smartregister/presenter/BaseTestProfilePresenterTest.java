package org.smartregister.presenter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.chw.agyw.contract.TestProfileContract;
import org.smartregister.chw.agyw.domain.MemberObject;
import org.smartregister.chw.agyw.presenter.BaseTestProfilePresenter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseTestProfilePresenterTest {

    @Mock
    private TestProfileContract.View view = Mockito.mock(TestProfileContract.View.class);

    @Mock
    private TestProfileContract.Interactor interactor = Mockito.mock(TestProfileContract.Interactor.class);

    @Mock
    private MemberObject memberObject = new MemberObject();

    private BaseTestProfilePresenter profilePresenter = new BaseTestProfilePresenter(view, interactor, memberObject);


    @Test
    public void fillProfileDataCallsSetProfileViewWithDataWhenPassedMemberObject() {
        profilePresenter.fillProfileData(memberObject);
        verify(view).setProfileViewWithData();
    }

    @Test
    public void fillProfileDataDoesntCallsSetProfileViewWithDataIfMemberObjectEmpty() {
        profilePresenter.fillProfileData(null);
        verify(view, never()).setProfileViewWithData();
    }

    @Test
    public void agywTestDatePeriodIsLessThanSeven() {
        profilePresenter.recordAgywButton("");
        verify(view).hideView();
    }

    @Test
    public void agywTestDatePeriodGreaterThanTen() {
        profilePresenter.recordAgywButton("OVERDUE");
        verify(view).setOverDueColor();
    }

    @Test
    public void agywTestDatePeriodIsMoreThanFourteen() {
        profilePresenter.recordAgywButton("EXPIRED");
        verify(view).hideView();
    }

    @Test
    public void refreshProfileBottom() {
        profilePresenter.refreshProfileBottom();
        verify(interactor).refreshProfileInfo(memberObject, profilePresenter.getView());
    }

    @Test
    public void saveForm() {
        profilePresenter.saveForm(null);
        verify(interactor).saveRegistration(null, view);
    }
}
