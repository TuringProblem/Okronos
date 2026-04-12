import statements for content using generic api + special cases:

// generic api
import static core.api.APIProvider.*;
// generic api data types
import core.api.pos.*;
import core.api.npc.*;
import core.api.player.*;
// reason specific api
import static core.reason.ReasonAPI.*;
// mappings between generic/reason api
import static core.reason.ReasonMappings.*;
// reason specific hooks that use reason specific types
import static core.reason.ReasonNPCHooks.*;


import statements for content using pure generic api:

// generic api
import static core.api.APIProvider.*;
// generic api data types
import core.api.pos.*;
import core.api.npc.*;
import core.api.player.*;
