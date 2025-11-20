package nl.miw.ch17.mmadevforce.kookkompas.service.mappers;

import nl.miw.ch17.mmadevforce.kookkompas.dto.NewKookKompasUserDTO;
import nl.miw.ch17.mmadevforce.kookkompas.model.KookKompasUser;

/**
 * @author Melanie van der Vlies
 * Doel van de class:
 */
public class KookKompasUserMapper {
    public static KookKompasUser fromDTO(NewKookKompasUserDTO newKookKompasUserDTO) {
        KookKompasUser kookKompasUser = new KookKompasUser();

        kookKompasUser.setUsername(newKookKompasUserDTO.getUsername());
        kookKompasUser.setPassword(newKookKompasUserDTO.getPassword());

        return kookKompasUser;
    }
}
