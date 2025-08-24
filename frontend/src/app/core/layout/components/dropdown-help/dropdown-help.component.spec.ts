import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DropdownHelpComponent } from './dropdown-help.component';

describe('DropdownHelpComponent', () => {
  let component: DropdownHelpComponent;
  let fixture: ComponentFixture<DropdownHelpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DropdownHelpComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DropdownHelpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
