package models;

import javax.persistence.*;

/**
 * Created by bharadwaj on 23/01/14.
 */
@Entity
@SequenceGenerator(name = "Token_generator", sequenceName = "test_sequence", allocationSize = 1, initialValue = 1)
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Token_generator")
    public Long id;

    public String name;

    public Test() {
    }
}
