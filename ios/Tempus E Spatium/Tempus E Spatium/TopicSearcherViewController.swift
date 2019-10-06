//
//  TopicSearcherViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 6/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import DropDown

class TopicSearcherViewController: UIViewController {
    @IBOutlet weak var mLocaleDropDown: DropDown!
    
    @IBOutlet weak var mTopicDropDown: DropDown!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        performSegue(withIdentifier: "launchRound1", sender: nil)
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }

}
