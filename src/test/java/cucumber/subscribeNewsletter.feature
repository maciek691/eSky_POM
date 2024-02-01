Feature: Subscribe for a newsletter

#    this will run before each scenario
  Background:
    Given I am landed on homepage
#@newsletter
#    this will run for each row in Examples
  Scenario Outline: Subscribe for a newsletter
    Given I am on the newsletter section
    When type my <email> address, check to box with special offers and click subscribe
    Then I should see a success message

    Examples:
      | email               |
      | tt8950332@gmail.com |

