create index item_name_idx on item using gin(lower(name) gin_trgm_ops);
