
@home
Feature: Naukri Application Home Page Functionalities
  Background:
    Given I navigate to Naukri Application
    And enter the credentials
	
  Scenario: Edit Carrer Profile
    When I naviagte to my profile
    And Click on carrer profile
    And I click on "preffered location", select/deslect a "location"
    And clcik on save button
    Then I should see profile updated as "Today"