package com.example.academic.service;

import com.example.academic.exception.DatabaseException;
import com.example.academic.model.Group;
import com.example.academic.repository.IGroupRepository;
import com.example.academic.repository.GroupRepository;

import java.util.List;

/**
 * Service pour la gestion des groupes
 */
public class GroupService {
    private final IGroupRepository groupRepository;

    public GroupService() {
        this.groupRepository = new GroupRepository();
    }

    public Group createGroup(String name, String description) {
        if (groupRepository.existsByName(name)) {
            throw new IllegalArgumentException("Un group avec le nom '" + name + "' existe déjà");
        }

        Group group = new Group(name, description);
        groupRepository.save(group);
        return group;
    }

    public Group findById(String id) throws DatabaseException {
        return groupRepository.findById(id);
    }

    public void updateGroup(Group group) throws DatabaseException {
        groupRepository.update(group);
    }

    public void deleteGroup(String id) throws DatabaseException {
        groupRepository.delete(id);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
}

